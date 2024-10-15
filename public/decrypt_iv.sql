create function public.decrypt_iv(bytea, bytea, bytea, text) returns bytea
    immutable
    strict
    parallel safe
    language c
as
$$
begin
-- missing source code
end;
$$;

alter function public.decrypt_iv(bytea, bytea, bytea, text) owner to postgres;

