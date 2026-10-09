select

    channel,

    count(*) as transaction_count,

    sum(units_sold) as total_units_sold,

    round(
        sum(revenue),
        2
    ) as total_revenue,

    round(
        sum(gross_profit),
        2
    ) as total_gross_profit,

    round(
        avg(discount),
        4
    ) as average_discount,

    round(
        avg(margin_rate),
        4
    ) as average_margin_rate

from {{ ref('fct_sales') }}

group by channel

order by total_revenue desc