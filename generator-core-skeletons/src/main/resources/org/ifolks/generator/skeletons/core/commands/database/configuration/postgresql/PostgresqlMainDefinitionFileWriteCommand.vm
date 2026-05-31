DROP SCHEMA IF EXISTS public CASCADE;
CREATE SCHEMA public;

-------------------------------------------------------------
---- FUNCTION normalize : unaccent and lowercase  ----
-------------------------------------------------------------
CREATE OR REPLACE FUNCTION "normalize"(text)
RETURNS text
IMMUTABLE
STRICT
LANGUAGE SQL
AS $$
SELECT translate(
    lower($1),
    'àâäèéêëîïôöûü',
    'aaaeeeeiioouu'
);
$$;


