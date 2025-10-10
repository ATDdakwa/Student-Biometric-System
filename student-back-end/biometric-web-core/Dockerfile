FROM openjdk:17-slim-buster as builder

MAINTAINER G.MHANDU

ADD target/biometric-web-core.jar  /opt/biometric-web-core.jar

WORKDIR /opt

COPY . /opt

ENTRYPOINT ["java", "-jar", "biometric-web-core.jar", "&"]