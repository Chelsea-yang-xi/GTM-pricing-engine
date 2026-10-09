from pathlib import Path
import json

import duckdb


BASE_DIR = Path(__file__).resolve().parents[1]

WAREHOUSE_FILE = (
        BASE_DIR
        / "dbt"
        / "retail_warehouse.duckdb"
)

MARGIN_RISK_THRESHOLD = 0.30


def build_context():

    connection = duckdb.connect(
        str(WAREHOUSE_FILE),
        read_only=True,
    )

    top_channel = connection.execute(
        """
        SELECT
            channel,
            total_revenue,
            total_gross_profit,
            average_margin_rate
        FROM mart_channel_performance
        ORDER BY total_revenue DESC
        LIMIT 1
        """
    ).fetchone()


    lowest_margin_segment = connection.execute(
        """
        SELECT
            campaign,
            channel,
            total_revenue,
            average_margin_rate
        FROM mart_pricing_effectiveness
        ORDER BY average_margin_rate ASC
        LIMIT 1
        """
    ).fetchone()


    high_risk_sku_count = connection.execute(
        """
        SELECT COUNT(*)
        FROM (
            SELECT sku
            FROM fct_sales
            GROUP BY sku
            HAVING AVG(margin_rate) < ?
        )
        """,
        [MARGIN_RISK_THRESHOLD],
    ).fetchone()[0]


    total_revenue = connection.execute(
        """
        SELECT
            ROUND(SUM(revenue), 2)
        FROM fct_sales
        """
    ).fetchone()[0]


    connection.close()


    return {
        "total_revenue": float(total_revenue),

        "top_revenue_channel": {
            "channel": top_channel[0],
            "revenue": float(top_channel[1]),
            "gross_profit": float(top_channel[2]),
            "average_margin_rate": float(top_channel[3]),
        },

        "lowest_margin_segment": {
            "campaign": lowest_margin_segment[0],
            "channel": lowest_margin_segment[1],
            "revenue": float(lowest_margin_segment[2]),
            "average_margin_rate": float(
                lowest_margin_segment[3]
            ),
        },

        "margin_risk_threshold":
            MARGIN_RISK_THRESHOLD,

        "high_risk_sku_count":
            high_risk_sku_count,
    }


def build_llm_prompt(context):

    return f"""
You are analysing retail pricing performance.

Use ONLY the validated metrics provided below.
Do not calculate or invent additional numbers.
Highlight revenue performance, margin risk,
and one actionable commercial observation.

METRICS:
{json.dumps(context, indent=2)}

Return:
1. Performance summary
2. Margin risk
3. Recommended action
""".strip()


if __name__ == "__main__":

    context = build_context()

    print("\nVALIDATED ANALYTICS CONTEXT\n")

    print(
        json.dumps(
            context,
            indent=2,
        )
    )

    print("\nLLM-READY PROMPT\n")

    print(
        build_llm_prompt(context)
    )