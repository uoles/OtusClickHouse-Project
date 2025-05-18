## Запуск приложений в Docker.

### Запуск PostgreSQL.

- linux:
```
docker run --name postgresql \
	-e POSTGRES_PASSWORD=passwordpg1234 \
	-e POSTGRES_USER=userpg \
	-e POSTGRES_DB=pgdb \
	-p 5433:5432 \
	-v "./postgresql/data":"/var/lib/postgresql/data" \
	-d postgres:latest
```

### Запуск приложения xml-parser.

- linux:
```
docker run --name postgresql \
	-e POSTGRES_PASSWORD=passwordpg1234 \
	-e POSTGRES_USER=userpg \
	-e POSTGRES_DB=pgdb \
	-p 5433:5432 \
	-v "./postgresql/data":"/var/lib/postgresql/data" \
	-d postgres:latest
```