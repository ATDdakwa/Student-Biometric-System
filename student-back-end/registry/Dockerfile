FROM openjdk:17-oracle
MAINTAINER firstmutual.co.zw

ADD target/registry-0.0.1-SNAPSHOT.jar  /opt/registry-0.0.1-SNAPSHOT.jar

WORKDIR /opt

COPY . /opt

ENTRYPOINT ["java","-jar","registry-0.0.1-SNAPSHOT.jar"]