FROM docker.io/library/openjdk:17
MAINTAINER Maksim Kulikov <max.uoles@rambler.ru>

COPY target/xml-parser.jar xml-parser.jar

EXPOSE 8090

CMD ["java","-jar","xml-parser.jar"]