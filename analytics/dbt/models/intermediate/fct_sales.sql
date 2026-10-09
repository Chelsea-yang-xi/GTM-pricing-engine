select

    transaction_id,

    sale_date,

    sku,

    channel,

    campaign,

    discount,

    selling_price,

    units_sold,

    inventory,

    revenue,

    total_cost,

    gross_profit,

    margin_rate

from {{ ref('stg_sales') }}