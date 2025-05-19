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

CREATE SEQUENCE IF NOT EXISTS pgclick.seq_valute_id;

select nextval('seq_valute_id');

select *
from pgclick.valute_data
where str_id = 'R01770'
limit 200


select count(*)
from pgclick.valute_data

drop table pgclick.valute_data;


select *
from pgclick.valute_data
where date = '19/05/2025';

select *
from pgclick.valute_data
order by id desc
limit 100;

delete from pgclick.valute_data
where id > 200000;

select count(1)
from pgclick.valute_data
where date = '19/05/2003';


INSERT INTO pgclick.valute_data( id, date, name, str_id, num_code, char_code, nominal, value )
    VALUES( nextval('pgclick.seq_valute_id'), '19/05/2009', 'Азербайджанский манат', 'R01020A', '944', 'AZN', '1', '47,5111' )
    ON CONFLICT ( date, str_id ) DO nothing;

INSERT INTO pgclick.valute_data( id, date, name, str_id, num_code, char_code, nominal, value )
    VALUES( nextval('pgclick.seq_valute_id'), '19/03/2000', 'Иен', 'R01820', '392', 'JPY', '100', '55,6950' )
    ON CONFLICT ( date, str_id ) DO nothing;

delete from pgclick.valute_data
where date = '19/05/2009';


ALTER SYSTEM SET wal_level = logical;


SHOW wal_level;


CREATE PUBLICATION pgclick_publication
	FOR TABLES IN SCHEMA pgclick
	with(publish = 'insert');


drop PUBLICATION pgclick_publication;



CREATE TABLE IF NOT EXISTS pgclick.debezium_offset_storage(id VARCHAR(36) NOT NULL,
    offset_key VARCHAR(1255),
    offset_val VARCHAR(1255),
    record_insert_ts TIMESTAMP NOT NULL,
    record_insert_seq INTEGER NOT NULL
);


select *
from pgclick.debezium_offset_storage;

drop table pgclick.debezium_offset_storage;


SELECT
   slot_name,
   active,
   pg_size_pretty(pg_wal_lsn_diff(pg_current_wal_lsn(), restart_lsn)) as slot_lag,
   pg_size_pretty(pg_wal_lsn_diff(pg_current_wal_lsn(), confirmed_flush_lsn)) as confirmed_lag
FROM pg_replication_slots
WHERE slot_name = 'pgclick_slot'

SELECT pg_drop_replication_slot('pgclick_slot');


