create table public.spa_booking
(
    id           integer generated by default as identity
        primary key,
    user_id      integer
        references public.person,
    procedure_id integer
        references public.spa_procedures,
    date         timestamp,
    status       varchar(50) default 'Зарегистрирован(а)'::character varying,
    employee_id  integer
        references public.spa_employees
)
    using ???;

alter table public.spa_booking
    owner to postgres;

