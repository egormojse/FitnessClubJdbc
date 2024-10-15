create function public.gen_random_uuid() returns uuid
    parallel safe
    language c
as
$$
begin
-- missing source code
end;
$$;

alter function public.gen_random_uuid() owner to postgres;

