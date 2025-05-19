
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

