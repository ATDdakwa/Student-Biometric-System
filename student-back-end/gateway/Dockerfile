FROM openjdk:17-oracle

MAINTAINER firstmutual.co.zw

ADD target/gateway-0.0.1-SNAPSHOT.jar  /opt/gateway-0.0.1-SNAPSHOT.jar

WORKDIR /opt

COPY . /opt

ENTRYPOINT ["java","-jar","gateway-0.0.1-SNAPSHOT.jar"]