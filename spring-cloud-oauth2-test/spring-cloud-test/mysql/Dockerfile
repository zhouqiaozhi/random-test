FROM mysql:9.0.0

ENV MYSQL_DATABASE=oauth2db MYSQL_ROOT_PASSWORD=root

ADD schemas/oauth2.sql /docker-entrypoint-initdb.d

EXPOSE 3306