FROM docker.io/library/openjdk:17
MAINTAINER Maksim Kulikov <max.uoles@rambler.ru>

COPY target/debezium.jar debezium.jar

EXPOSE 8091

CMD ["java","-jar","debezium.jar"]