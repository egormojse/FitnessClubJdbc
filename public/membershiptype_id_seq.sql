create sequence public.membershiptype_id_seq;

alter sequence public.membershiptype_id_seq owner to postgres;

alter sequence public.membershiptype_id_seq owned by public.membership_type.id;

