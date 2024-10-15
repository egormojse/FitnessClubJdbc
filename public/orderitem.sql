create table public.orderitem
(
    id         integer generated by default as identity
        primary key,
    order_id   integer
        references public."order",
    product_id integer
        references public.product,
    quantity   integer,
    price      numeric(10, 2)
)
    using ???;

alter table public.orderitem
    owner to postgres;

