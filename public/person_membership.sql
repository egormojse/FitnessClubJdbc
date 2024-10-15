create table public.person_membership
(
    id                   integer generated by default as identity
        primary key,
    person_id            integer
        references public.person,
    membership_id        integer
        references public.membership_type,
    start_date           date,
    end_date             date,
    remaining_gym_visits integer,
    remaining_spa_visits integer
)
    using ???;

alter table public.person_membership
    owner to postgres;

