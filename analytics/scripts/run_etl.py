from pathlib import Path

import pandas as pd


BASE_DIR = Path(__file__).resolve().parents[1]

RAW_FILE = (
        BASE_DIR
        / "data"
        / "raw"
        / "sales.csv"
)

OUTPUT_FILE = (
        BASE_DIR
        / "data"
        / "processed"
        / "sales_clean.parquet"
)


REQUIRED_COLUMNS = {
    "transaction_id",
    "date",
    "sku",
    "category",
    "channel",
    "campaign",
    "rrp",
    "cost",
    "discount",
    "selling_price",
    "units_sold",
    "inventory",
}


def extract():

    print(
        "Extracting raw sales data..."
    )

    return pd.read_csv(
        RAW_FILE
    )


def validate_schema(df):

    missing_columns = (
            REQUIRED_COLUMNS
            - set(df.columns)
    )

    if missing_columns:

        raise ValueError(
            "Missing required columns: "
            + str(
                sorted(
                    missing_columns
                )
            )
        )


def transform(df):

    print(
        "Transforming sales data..."
    )

    df = df.copy()


    df["date"] = pd.to_datetime(
        df["date"]
    )


    # Remove duplicate transactions
    df = df.drop_duplicates(
        subset=[
            "transaction_id"
        ]
    )


    # Basic business validation
    df = df[
        (df["rrp"] > 0)
        & (df["cost"] >= 0)
        & (
            df["discount"]
            .between(
                0,
                1,
            )
        )
        & (
                df["units_sold"]
                > 0
        )
        ].copy()


    # Derived measures
    df["revenue"] = (
            df["selling_price"]
            * df["units_sold"]
    )


    df["total_cost"] = (
            df["cost"]
            * df["units_sold"]
    )


    df["gross_profit"] = (
            df["revenue"]
            - df["total_cost"]
    )


    df["margin_rate"] = (
            df["gross_profit"]
            / df["revenue"]
    )


    # Useful time dimensions
    df["year"] = (
        df["date"].dt.year
    )

    df["month"] = (
        df["date"].dt.month
    )


    return df


def load(df):

    print(
        "Loading processed dataset..."
    )

    OUTPUT_FILE.parent.mkdir(
        parents=True,
        exist_ok=True,
    )

    df.to_parquet(
        OUTPUT_FILE,
        index=False,
    )

    print(
        f"Loaded {len(df):,} rows"
    )

    print(
        f"Saved to: {OUTPUT_FILE}"
    )


def run_pipeline():

    df = extract()

    validate_schema(df)

    transformed = transform(df)

    load(transformed)


if __name__ == "__main__":

    run_pipeline()