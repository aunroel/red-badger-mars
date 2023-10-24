FROM maven:3.8.4-openjdk-17 AS MAVEN_BUILD

MAINTAINER roman.matios@hey.com

COPY ./ ./

RUN mvn clean package

FROM openjdk:17

# copy only the artifacts we need from the first stage and discard the rest
COPY --from=MAVEN_BUILD /target/martian-robots-1.0-SNAPSHOT-jar-with-dependencies.jar /martian-robots.jar

CMD ["java", "-jar", "/martian-robots.jar"]