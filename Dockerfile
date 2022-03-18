# syntax=docker/dockerfile:1

FROM openjdk:11

WORKDIR /app

COPY .mvn/ .mvn
COPY mvnw pom.xml .
RUN ./mvnw clean dependency:go-offline

COPY src/ src

RUN adduser issuetracker
RUN chown -R issuetracker /app
USER issuetracker

CMD ["./mvnw", "spring-boot:run"]