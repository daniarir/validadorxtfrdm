FROM quay.io/devfile/maven:3.8.1-openjdk-11-slim

WORKDIR /build

# Build dependency offline to streamline build
COPY pom.xml .
COPY settings.xml .
COPY src src
RUN mvn -s settings.xml dependency:go-offline package -Dmaven.test.skip=true

RUN cp ./target/xtf-validador-1.1.jar xtf-validador-1.1.jar

EXPOSE 8081
ENTRYPOINT [ "java", "-jar", "xtf-validador-1.1.jar", "--server.port=8081" ]

WORKDIR /opt
RUN mkdir -p /opt/descargarXTF
RUN mkdir -p /opt/modelos
RUN mkdir -p /opt/log

RUN chmod -R 777 /opt
USER root
RUN apt-get update && apt-get install -y binutils libproj-dev gdal-bin
WORKDIR /build