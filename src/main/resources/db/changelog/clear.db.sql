--changeset znakarik:clear-postgres dbms:postgresql runAlways:true
DROP SCHEMA if EXISTS public CASCADE;
CREATE SCHEMA public;
