CREATE SCHEMA pgclick;

CREATE TABLE IF NOT EXISTS pgclick.valute_data(
	id bigint NOT NULL PRIMARY KEY,
	date VARCHAR(36) NOT NULL,
	name VARCHAR(200) NOT NULL,
	str_id VARCHAR(20) NOT NULL,
	num_code VARCHAR(20) NOT NULL,
	char_code VARCHAR(20) NOT NULL,
	nominal VARCHAR(20) NOT NULL,
	value VARCHAR(50) NOT null
);

CREATE UNIQUE INDEX idx_valute_pk
ON pgclick.valute_data(id);

CREATE UNIQUE INDEX idx_valute_data
ON pgclick.valute_data(date, str_id);

CREATE SEQUENCE IF NOT EXISTS seq_valute_id;

select nextval('seq_valute_id');

select *
from pgclick.valute_data
where str_id = 'R01770'
limit 200

----

ALTER SYSTEM SET wal_level = logical;

SHOW wal_level;

CREATE PUBLICATION pgclick_publication FOR TABLES IN SCHEMA pgclick;

CREATE TABLE IF NOT EXISTS pgclick.debezium_offset_storage(
    id VARCHAR(36) NOT NULL,
    offset_key VARCHAR(1255),
    offset_val VARCHAR(1255),
    record_insert_ts TIMESTAMP NOT NULL,
    record_insert_seq INTEGER NOT NULL
);

select *
from pgclick.debezium_offset_storage;

SELECT
   slot_name,
   active,
   pg_size_pretty(pg_wal_lsn_diff(pg_current_wal_lsn(), restart_lsn)) as slot_lag,
   pg_size_pretty(pg_wal_lsn_diff(pg_current_wal_lsn(), confirmed_flush_lsn)) as confirmed_lag
FROM pg_replication_slots
WHERE slot_name = 'pgclick_slot';