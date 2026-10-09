from pathlib import Path

import numpy as np
import pandas as pd


RANDOM_SEED = 42

NUM_PRODUCTS = 100
NUM_RECORDS = 50_000

CHANNELS = [
    "Amazon",
    "Coolblue",
    "Bol.com",
    "MediaMarkt",
]

CATEGORIES = [
    "Charging",
    "Audio",
    "Smart Home",
    "Camera",
]

CAMPAIGNS = [
    "Regular",
    "Black Friday",
    "Christmas",
    "Clearance",
]


BASE_DIR = Path(__file__).resolve().parents[1]

RAW_DATA_DIR = BASE_DIR / "data" / "raw"

OUTPUT_FILE = RAW_DATA_DIR / "sales.csv"


def create_products(rng):
    products = []

    for number in range(1, NUM_PRODUCTS + 1):

        sku = f"SKU{number:04d}"

        category = rng.choice(CATEGORIES)

        cost = round(
            rng.uniform(10, 120),
            2,
        )

        rrp = round(
            cost * rng.uniform(1.4, 2.2),
            2,
            )

        products.append(
            {
                "sku": sku,
                "category": category,
                "cost": cost,
                "rrp": rrp,
            }
        )

    return pd.DataFrame(products)


def generate_sales_data():

    rng = np.random.default_rng(
        RANDOM_SEED
    )

    products = create_products(rng)

    dates = pd.date_range(
        start="2025-01-01",
        end="2025-12-31",
        freq="D",
    )

    rows = []

    for transaction_id in range(
            1,
            NUM_RECORDS + 1,
    ):

        product = products.iloc[
            rng.integers(
                0,
                len(products),
            )
        ]

        date = rng.choice(dates)

        channel = rng.choice(
            CHANNELS,
            p=[
                0.40,
                0.20,
                0.20,
                0.20,
            ],
        )

        month = pd.Timestamp(date).month


        # Seasonal campaign logic
        if month == 11:

            campaign = rng.choice(
                [
                    "Black Friday",
                    "Regular",
                ],
                p=[
                    0.70,
                    0.30,
                ],
            )

        elif month == 12:

            campaign = rng.choice(
                [
                    "Christmas",
                    "Regular",
                ],
                p=[
                    0.60,
                    0.40,
                ],
            )

        else:

            campaign = rng.choice(
                [
                    "Regular",
                    "Clearance",
                ],
                p=[
                    0.90,
                    0.10,
                ],
            )


        # Campaign discount behaviour
        if campaign == "Black Friday":

            discount = rng.uniform(
                0.15,
                0.30,
            )

        elif campaign == "Christmas":

            discount = rng.uniform(
                0.10,
                0.20,
            )

        elif campaign == "Clearance":

            discount = rng.uniform(
                0.20,
                0.40,
            )

        else:

            discount = rng.uniform(
                0.00,
                0.10,
            )


        discount = round(
            discount,
            4,
        )


        selling_price = round(
            product["rrp"]
            * (1 - discount),
            2,
            )


        # Higher discounts generally increase demand
        demand_multiplier = (
                1
                + discount * 3
        )


        # Seasonal demand boost
        if campaign in [
            "Black Friday",
            "Christmas",
        ]:
            demand_multiplier *= 1.5


        units_sold = max(
            1,
            int(
                rng.poisson(
                    3
                    * demand_multiplier
                )
            ),
        )


        inventory = int(
            rng.integers(
                0,
                500,
            )
        )


        rows.append(
            {
                "transaction_id":
                    transaction_id,

                "date":
                    pd.Timestamp(date),

                "sku":
                    product["sku"],

                "category":
                    product["category"],

                "channel":
                    channel,

                "campaign":
                    campaign,

                "rrp":
                    product["rrp"],

                "cost":
                    product["cost"],

                "discount":
                    discount,

                "selling_price":
                    selling_price,

                "units_sold":
                    units_sold,

                "inventory":
                    inventory,
            }
        )


    sales = pd.DataFrame(rows)

    RAW_DATA_DIR.mkdir(
        parents=True,
        exist_ok=True,
    )

    sales.to_csv(
        OUTPUT_FILE,
        index=False,
    )

    print(
        f"Generated {len(sales):,} rows"
    )

    print(
        f"Saved to: {OUTPUT_FILE}"
    )

    print()

    print(
        sales.head()
    )


if __name__ == "__main__":
    generate_sales_data()