create table public.spa_employee_procedure
(
    id           integer generated by default as identity
        primary key,
    employee_id  integer not null
        references public.spa_employees
            on delete cascade,
    procedure_id integer not null
        references public.spa_procedures
            on delete cascade,
    price        numeric(10, 2)
)
    using ???;

alter table public.spa_employee_procedure
    owner to postgres;

