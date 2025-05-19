#!/bin/bash
docker stop postgresql
docker rm postgresql

docker run --name postgresql \
    -e POSTGRES_PASSWORD=passwordpg1234 \
    -e POSTGRES_USER=userpg \
    -e POSTGRES_DB=pgdb \
    -p 5433:5432 \
    -v "C:\Users\uoles\IdeaProjects\OtusClickHouse-Project\docker\postgresql\data":"/var/lib/postgresql/data" \
    -d postgres:latest
