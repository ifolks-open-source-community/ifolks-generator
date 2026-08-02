-------------------------------------------------------------
--------------------- PURGE USER SCHEMA  --------------------
-------------------------------------------------------------
BEGIN

FOR c IN (SELECT table_name FROM user_tables) LOOP
EXECUTE IMMEDIATE ('DROP TABLE "' || c.table_name || '" CASCADE CONSTRAINTS');
END LOOP;

FOR s IN (SELECT sequence_name FROM user_sequences) LOOP
EXECUTE IMMEDIATE ('DROP SEQUENCE ' || s.sequence_name);
END LOOP;

END;
/

-------------------------------------------------------------
---- FUNCTION UNACCENT : unaccent and lowercase  ----
-------------------------------------------------------------
CREATE OR REPLACE FUNCTION UNACCENT(ARG IN VARCHAR2)
RETURN VARCHAR2
IS
BEGIN
RETURN TRANSLATE(
LOWER(ARG),
'àâäèéêëîïôöûü',
'aaaeeeeiioouu'
);
END NORMALIZE;
/
