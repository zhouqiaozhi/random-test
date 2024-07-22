FROM mysql:9.0.0

ENV MYSQL_DATABASE=testdb MYSQL_ROOT_PASSWORD=root

ADD schemas/test.sql /docker-entrypoint-initdb.d

EXPOSE 3306