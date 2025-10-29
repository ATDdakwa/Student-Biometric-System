FROM openjdk:17-slim-buster as builder

MAINTAINER M.Chikuruwo

ADD target/user-service-0.0.1-SNAPSHOT.jar /opt/user-service-0.0.1-SNAPSHOT.jar

WORKDIR /opt

COPY . /opt

ENTRYPOINT ["java", "-jar", "user-service-0.0.1-SNAPSHOT.jar", "&"]