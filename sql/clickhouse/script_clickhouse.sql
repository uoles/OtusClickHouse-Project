
CREATE TABLE valute_data
(
	id UInt64,
    date Date,
    name String,
    str_id String,
    num_code String,
    char_code String,
    nominal String,
    value String
)
ENGINE = MergeTree
ORDER BY (id, date, str_id);

DROP TABLE valute_data;

CREATE TABLE valute_data_queue
(
	id UInt64,
    date String,
    name String,
    str_id String,
    num_code String,
    char_code String,
    nominal String,
    value String
)
ENGINE = Kafka
SETTINGS
    kafka_broker_list = 'kafka:9092',
    kafka_topic_list = 'valute_topic',
    kafka_group_name = 'valute_group',
    kafka_format = 'JSONEachRow',
  	kafka_row_delimiter = '\n',
    kafka_num_consumers = 1,
    kafka_skip_broken_messages = 10,
    kafka_thread_per_consumer = 0;

DROP TABLE valute_data_queue;

CREATE MATERIALIZED VIEW valute_data_queue_mv TO valute_data
(
	id UInt64,
    date Date,
    name String,
    str_id String,
    num_code String,
    char_code String,
    nominal String,
    value String
) AS
SELECT id, parseDateTimeBestEffortOrNull(date,'Europe/Moscow') AS date, name, str_id, num_code, char_code, nominal, value
FROM valute_data_queue;

DROP TABLE valute_data_queue_mv;

select *
from valute_data_queue_mv
order by date desc;


select count(1)
from valute_data_queue_mv
where date = toDate('2000-03-19');

SELECT toDate('2000-03-19') AS date;

SELECT parseDateTimeBestEffortOrNull('19/05/2002','Europe/Moscow') AS parsed_date;


----

select count()
from (
	SELECT toStartOfDay(toDateTime(`date`)) AS `date_5fc732`, max(`value`) AS `MAX(value)_e41d49`
	FROM `my_database`.`valute_data_queue_mv`
	WHERE `name` IN ('Швейцарский франк') AND `date` >= toDate('2010-01-01') AND `date` < toDate('2025-05-19') GROUP BY toStartOfDay(toDateTime(`date`)) ORDER BY `MAX(value)_e41d49` DESC
	LIMIT 10000
)

select *
from valute_data_queue_mv
where name = 'Белорусских рублей'
order by date;

select count()
from (
	SELECT toStartOfDay(toDateTime(`date`)) AS `date_5fc732`, `name` AS `name_b06893`, max(`value`) AS `MAX(value)_e41d49`
	FROM `my_database`.`valute_data_queue_mv`
	WHERE `date` >= toDate('2016-01-01') AND `date` < toDate('2025-05-19') AND `name` IN ('Норвежских крон', 'Белорусских рублей')
	GROUP BY toStartOfDay(toDateTime(`date`)), `name`
	ORDER BY `date_5fc732` DESC
	LIMIT 10000
)
where name_b06893 = 'Белорусских рублей';

-----

CREATE MATERIALIZED VIEW valute_data_evro_mv TO valute_data_evro
(
	id UInt64,
    date Date,
    name String,
    str_id String,
    num_code String,
    char_code String,
    nominal UInt32,
    value Float32
) AS
SELECT
	id,
	parseDateTimeBestEffortOrNull(date,'Europe/Moscow') AS date,
	name,
	str_id,
	num_code,
	char_code,
	toInt32OrNull(nominal) as nominal,
	toFloat32OrNull(replaceOne(value, ',', '.')) / nominal as value
FROM valute_data_queue
WHERE name = 'Евро';

DROP TABLE valute_data_evro_mv;

CREATE TABLE valute_data_evro
(
	id UInt64,
    date Date,
    name String,
    str_id String,
    num_code String,
    char_code String,
    nominal UInt32,
    value Float32
)
ENGINE = MergeTree
ORDER BY (id, date, str_id);

DROP TABLE valute_data_evro;

select count()
from valute_data_evro_mv;


select *
from valute_data_evro_mv
order by id desc;

---

CREATE MATERIALIZED VIEW valute_data_dirham_mv TO valute_data_dirham
(
	id UInt64,
    date Date,
    name String,
    str_id String,
    num_code String,
    char_code String,
    nominal UInt32,
    value Float32
) AS
SELECT
	id,
	parseDateTimeBestEffortOrNull(date,'Europe/Moscow') AS date,
	name,
	str_id,
	num_code,
	char_code,
	toInt32OrNull(nominal) as nominal,
	toFloat32OrNull(replaceOne(value, ',', '.')) /nominal as value
FROM valute_data_queue
WHERE name = 'Дирхам ОАЭ';

DROP TABLE valute_data_dirham_mv;

CREATE TABLE valute_data_dirham
(
	id UInt64,
    date Date,
    name String,
    str_id String,
    num_code String,
    char_code String,
    nominal UInt32,
    value Float32
)
ENGINE = MergeTree
ORDER BY (id, date, str_id);

DROP TABLE valute_data_dirham;

select count()
from valute_data_dirham;


select *
from valute_data_dirham_mv
order by date;

---

CREATE MATERIALIZED VIEW valute_data_tenge_mv TO valute_data_tenge
(
	id UInt64,
    date Date,
    name String,
    str_id String,
    num_code String,
    char_code String,
    nominal UInt32,
    value Float32
) AS
SELECT
	id,
	parseDateTimeBestEffortOrNull(date,'Europe/Moscow') AS date,
	name,
	str_id,
	num_code,
	char_code,
	toInt32OrNull(nominal) as nominal,
	toFloat32OrNull(replaceOne(value, ',', '.')) /nominal as value
FROM valute_data_queue
WHERE name = 'Тенге';

DROP TABLE valute_data_tenge_mv;

CREATE TABLE valute_data_tenge
(
	id UInt64,
    date Date,
    name String,
    str_id String,
    num_code String,
    char_code String,
    nominal UInt32,
    value Float32
)
ENGINE = MergeTree
ORDER BY (id, date, str_id);

DROP TABLE valute_data_tenge;

select *
from valute_data_tenge_mv
order by date;

