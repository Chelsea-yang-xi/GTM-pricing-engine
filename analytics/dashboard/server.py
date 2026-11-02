
from datetime import date
from http.server import BaseHTTPRequestHandler, HTTPServer
from pathlib import Path
from urllib.parse import parse_qs, urlparse
import json
import traceback

import duckdb


BASE_DIR = Path(__file__).resolve().parent
DB_PATH = BASE_DIR.parent / "dbt" / "retail_warehouse.duckdb"
HTML_PATH = BASE_DIR / "index.html"

HOST = "127.0.0.1"
PORT = 8501


def get_dashboard_data(query):
    start = query.get("start", [""])[0]
    end = query.get("end", [""])[0]
    channel = query.get("channel", ["All"])[0]

    # Validate dates before constructing queries
    if start:
        date.fromisoformat(start)
    if end:
        date.fromisoformat(end)
    if start and end and start > end:
        raise ValueError("Start date must not be after end date")

    where = []
    params = []

    if start:
        where.append("sale_date >= ?")
        params.append(start)

    if end:
        where.append("sale_date <= ?")
        params.append(end)

    if channel != "All":
        where.append("channel = ?")
        params.append(channel)

    condition = " AND ".join(where) if where else "TRUE"

    with duckdb.connect(str(DB_PATH), read_only=True) as con:

        # Dataset metadata
        available = con.execute("""
            SELECT
                CAST(MIN(sale_date) AS VARCHAR),
                CAST(MAX(sale_date) AS VARCHAR)
            FROM fct_sales
        """).fetchone()

        channels = [
            row[0]
            for row in con.execute("""
                SELECT DISTINCT channel
                FROM fct_sales
                ORDER BY channel
            """).fetchall()
        ]

        # Headline metrics
        metrics = con.execute(f"""
            SELECT
                COUNT(*),
                COALESCE(SUM(revenue), 0),
                COALESCE(SUM(gross_profit), 0)
            FROM fct_sales
            WHERE {condition}
        """, params).fetchone()

        transactions, revenue, profit = metrics
        revenue = float(revenue)
        profit = float(profit)

        margin = (
            profit / revenue * 100
            if revenue != 0 else 0
        )

        # Revenue distribution by channel
        channel_rows = con.execute(f"""
            SELECT
                channel,
                SUM(revenue) AS revenue,
                SUM(gross_profit) AS profit
            FROM fct_sales
            WHERE {condition}
            GROUP BY channel
            ORDER BY revenue DESC
        """, params).fetchall()

        # Top five SKUs
        sku_rows = con.execute(f"""
            SELECT
                sku,
                SUM(units_sold) AS units,
                SUM(revenue) AS revenue,
                SUM(gross_profit) AS profit
            FROM fct_sales
            WHERE {condition}
            GROUP BY sku
            ORDER BY revenue DESC
            LIMIT 5
        """, params).fetchall()

    channel_results = [
        {
            "channel": row[0],
            "revenue": round(float(row[1]), 2),
            "profit": round(float(row[2]), 2),
        }
        for row in channel_rows
    ]

    sku_results = []

    for sku, units, sku_revenue, sku_profit in sku_rows:
        sku_revenue = float(sku_revenue)
        sku_profit = float(sku_profit)

        sku_results.append({
            "sku": sku,
            "units": int(units),
            "revenue": round(sku_revenue, 2),
            "margin": round(
                sku_profit / sku_revenue * 100, 1
            ) if sku_revenue else 0,
        })

    # Deterministic insight, not an LLM prediction
    insights = []

    if channel_results and revenue:
        top = channel_results[0]
        share = top["revenue"] / revenue * 100

        insights.append(
            f'{top["channel"]} generated '
            f'{share:.1f}% of selected revenue.'
        )

    if transactions:
        insights.append(
            f'{transactions:,} transactions in the selected period.'
        )

    insights.append(
        f'Weighted gross margin: {margin:.1f}%.'
    )

    return {
        "date_min": available[0],
        "date_max": available[1],
        "channels": channels,
        "metrics": {
            "transactions": transactions,
            "revenue": round(revenue, 2),
            "gross_profit": round(profit, 2),
            "margin_rate": round(margin, 1),
        },
        "by_channel": channel_results,
        "top_skus": sku_results,
        "insights": insights,
    }


class DashboardHandler(BaseHTTPRequestHandler):

    def send_data(self, body, content_type, status=200):
        self.send_response(status)
        self.send_header("Content-Type", content_type)
        self.send_header("Cache-Control", "no-store")
        self.end_headers()
        self.wfile.write(body)

    def do_GET(self):
        path = urlparse(self.path)

        if path.path == "/":
            html = HTML_PATH.read_bytes()
            self.send_data(html, "text/html; charset=utf-8")
            return

        if path.path == "/api/dashboard":
            try:
                data = get_dashboard_data(
                    parse_qs(path.query)
                )
                payload = json.dumps(data).encode("utf-8")
                self.send_data(
                    payload, "application/json; charset=utf-8"
                )
            except ValueError as error:
                payload = json.dumps({
                    "error": str(error)
                }).encode("utf-8")
                self.send_data(
                    payload, "application/json", 400
                )
            except Exception:
                traceback.print_exc()
                payload = json.dumps({
                    "error": "Dashboard query failed. Check terminal."
                }).encode("utf-8")
                self.send_data(
                    payload, "application/json", 500
                )
            return

        self.send_data(b"Not found", "text/plain", 404)


if __name__ == "__main__":
    if not DB_PATH.exists():
        raise FileNotFoundError(
            f"Warehouse not found: {DB_PATH}\n"
            "Run dbt build first."
        )

    print(f"Database: {DB_PATH}")
    print(f"Open: http://{HOST}:{PORT}")

    HTTPServer((HOST, PORT), DashboardHandler).serve_forever()
