FROM openjdk:17-slim-buster as builder

MAINTAINER G.MHANDU

ADD target/hcp-web-core-0.0.1-SNAPSHOT.jar  /opt/hcp-web-core-0.0.1-SNAPSHOT.jar

WORKDIR /opt

COPY . /opt

ENTRYPOINT ["java", "-jar", "hcp-web-core-0.0.1-SNAPSHOT.jar", "&"]