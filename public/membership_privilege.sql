create table public.membership_privilege
(
    membership_type_id integer not null
        references public.membership_type,
    privilege_id       integer not null
        references public.privilege,
    primary key (membership_type_id, privilege_id)
)
    using ???;

alter table public.membership_privilege
    owner to postgres;

