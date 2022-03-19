# # syntax=docker/dockerfile:1


### Maven build stage ###
FROM maven:3.8.4-openjdk-11 AS build
WORKDIR /tmp

COPY pom.xml .
COPY src/ src

RUN mvn -B -f pom.xml -s /usr/share/maven/ref/settings-docker.xml \
    clean dependency:go-offline \
    dependency:build-classpath -Dmdep.outputFile=classpath.txt -Dmdep.localRepoProperty=/var/maven/.m2/repository \
    compile
RUN sed 's|.*|-cp "/app/target/classes:&"|' classpath.txt > classpath.argfile

### JRE runtime stage ###
FROM openjdk:11-jre
WORKDIR /app

COPY --from=build /usr/share/maven/ref /var/maven/.m2
COPY --from=build /tmp/target/classes /app/target/classes
COPY --from=build /tmp/classpath.argfile /tmp

RUN adduser issuetracker
USER issuetracker

CMD ["java", "@/tmp/classpath.argfile", "com.snetsrac.issuetracker.IssueTrackerApplication"]
