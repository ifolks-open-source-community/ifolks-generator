DROP SCHEMA IF EXISTS public CASCADE;
CREATE SCHEMA public;

-------------------------------------------------------------
---- FUNCTION UNACCENT : unaccent and lowercase  ----
-------------------------------------------------------------
CREATE OR REPLACE FUNCTION "UNACCENT"(text)
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
