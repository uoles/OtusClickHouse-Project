
CREATE TABLE valute_data
(
    c_date String,
    c_name String,
    c_str_id String,
    c_num_code String,
    c_char_code String,
    c_nominal String,
    c_value String
)
ENGINE = MergeTree 
ORDER BY (c_date, c_str_id);

CREATE TABLE valute_data_queue
(
    c_date String,
    c_name String,
    c_str_id String,
    c_num_code String,
    c_char_code String,
    c_nominal String,
    c_value String
)
ENGINE = Kafka
SETTINGS 
    kafka_broker_list = 'kafka:9092', 
    kafka_topic_list = 'valute_topic',
    kafka_group_name = 'valute_group',
    kafka_format = 'CSV', 
    kafka_num_consumers = 1, 
    kafka_skip_broken_messages = 10,
    kafka_thread_per_consumer = 0;

CREATE MATERIALIZED VIEW valute_data_queue_mv TO valute_data
( 
    c_date String,
    c_name String,
    c_str_id String,
    c_num_code String,
    c_char_code String,
    c_nominal String,
    c_value String
) AS SELECT c_date, c_name, c_str_id, c_num_code, c_char_code, c_nominal, c_value  
FROM valute_data_queue;


select *
from valute_data_queue_mv;


