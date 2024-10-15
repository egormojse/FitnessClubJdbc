create function public.pgp_armor_headers(text, out key text, out value text) returns setof setof record
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

alter function public.pgp_armor_headers(text, out text, out text) owner to postgres;

