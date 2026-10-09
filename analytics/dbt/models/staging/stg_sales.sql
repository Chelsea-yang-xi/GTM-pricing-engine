with source as (

    select *
    from read_parquet(
        '../data/processed/sales_clean.parquet'
    )

),

renamed as (

    select

        transaction_id,

        cast(date as date) as sale_date,
        -- abstraction boundary

        sku,

        category,

        channel,

        campaign,

        rrp,

        cost,

        discount,

        selling_price,

        units_sold,

        inventory,

        revenue,

        total_cost,

        gross_profit,

        margin_rate,

        year,

        month

    from source

)

select *
from renamed