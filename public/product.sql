create table public.product
(
    id          integer generated by default as identity
        primary key,
    name        varchar(50),
    description text,
    price       numeric(10, 2),
    category    varchar(50),
    stock       integer
)
    using ???;

alter table public.product
    owner to postgres;
