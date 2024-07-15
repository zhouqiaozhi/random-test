FROM mysql:9.0.0

ENV MYSQL_DATABASE=batchdb MYSQL_ROOT_PASSWORD=root

ADD schemas/batch.sql /docker-entrypoint-initdb.d
ADD schemas/test.sql /docker-entrypoint-initdb.d

EXPOSE 3306