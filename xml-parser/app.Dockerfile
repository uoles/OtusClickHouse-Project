FROM docker.io/library/openjdk:17
MAINTAINER Maksim Kulikov <max.uoles@rambler.ru>

COPY target/xml-parser.jar xml-parser.jar

ENV TZ=Europe/Moscow
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

EXPOSE 8090

CMD ["java","-jar","xml-parser.jar"]