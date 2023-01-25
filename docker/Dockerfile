FROM maven:3.8.6-openjdk-18-slim AS build
COPY src /home/sigma/src
COPY pom.xml /home/sigma
#RUN mvn -f /home/app/pom.xml clean package
RUN mvn /home/sigma/pom.xml clean install -Dmaven.test.skip -Dcheckstyle.skip

FROM openjdk:18.0.2.1-jdk-slim-bullseye
COPY --from=build /home/sigma/target/sigma-*.jar /app/sigma.jar
EXPOSE 8081
ENTRYPOINT ["java","-jar","/app/sigma.jar"]