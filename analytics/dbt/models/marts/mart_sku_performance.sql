select

    s.sku,

    p.category,

    count(*) as transaction_count,

    sum(s.units_sold) as total_units_sold,

    round(
        sum(s.revenue),
        2
    ) as total_revenue,

    round(
        sum(s.gross_profit),
        2
    ) as total_gross_profit,

    round(
        avg(s.margin_rate),
        4
    ) as average_margin_rate

from {{ ref('fct_sales') }} as s

left join {{ ref('dim_product') }} as p
    on s.sku = p.sku

group by

    s.sku,

    p.category

order by total_revenue desc