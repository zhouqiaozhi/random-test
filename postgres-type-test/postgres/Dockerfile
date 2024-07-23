FROM postgres:17beta2

ENV POSTGRES_DB=testdb POSTGRES_PASSWORD=root

ADD schemas/test.sql /docker-entrypoint-initdb.d

EXPOSE 5432