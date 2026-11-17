FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /workspace
COPY pom.xml ./
RUN mvn -B -ntp dependency:go-offline
COPY src ./src
RUN mvn -B -ntp package -DskipTests

FROM eclipse-temurin:25-jre-alpine
RUN addgroup -S app && adduser -S app -G app
WORKDIR /app
COPY --from=build --chown=app:app /workspace/target/springtemplate.jar app.jar
USER app
EXPOSE 8080
HEALTHCHECK --interval=30s --timeout=5s --start-period=60s --retries=3 CMD wget -q -O /dev/null http://localhost:8080/actuator/health/liveness || exit 1
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
