FROM maven:3.9.9-eclipse-temurin-21 AS build

WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn -B -DskipTests package

FROM eclipse-temurin:21-jre

WORKDIR /app
RUN groupadd --system app && useradd --system --gid app --home-dir /app app
COPY --from=build --chown=app:app /app/target/auth-api-0.0.1-SNAPSHOT.jar app.jar

USER app
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
