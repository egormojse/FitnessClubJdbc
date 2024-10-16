create table public."order"
(
    id          integer generated by default as identity
        primary key,
    user_id     integer
        references public.person,
    order_date  timestamp,
    total_price numeric(10, 2),
    status      varchar(50) default 'Обрабатывается'::character varying
)
    using ???;

alter table public."order"
    owner to postgres;

