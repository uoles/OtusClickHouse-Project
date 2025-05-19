#!/bin/bash
docker build -f app.Dockerfile -t uoles/debezium:1.0.1 .

docker stop debezium
docker rm debezium

docker run -d --name debezium \
    --network=host \
	  --publish 8091:8091 \
    uoles/debezium:1.0.1