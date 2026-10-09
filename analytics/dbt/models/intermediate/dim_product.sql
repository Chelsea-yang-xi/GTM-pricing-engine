select distinct

    sku,

    category,

    rrp,

    cost

from {{ ref('stg_sales') }}

-- dim_product relies on stg_sales - data lineage
/*
dim_product
   = describes product

   fct_sales
   = records business events
*/