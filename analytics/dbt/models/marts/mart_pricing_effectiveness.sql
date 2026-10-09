select

    campaign,

    channel,

    count(*) as transaction_count,

    round(
        avg(discount),
        4
    ) as average_discount,

    round(
        avg(units_sold),
        2
    ) as average_units_sold,

    round(
        sum(revenue),
        2
    ) as total_revenue,

    round(
        sum(gross_profit),
        2
    ) as total_gross_profit,

    round(
        avg(margin_rate),
        4
    ) as average_margin_rate

from {{ ref('fct_sales') }}

group by

    campaign,

    channel

order by

    campaign,

    total_revenue desc