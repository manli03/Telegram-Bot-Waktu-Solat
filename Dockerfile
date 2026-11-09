# Multi-stage build: no local Java/Maven needed, just `docker compose up -d --build`
# or `docker build -t telegram-bot-waktu-solat .`

# ---- Build stage ----
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /src
# Cache dependencies separately from source changes
COPY pom.xml .
RUN mvn -B -q dependency:go-offline
COPY src ./src
RUN mvn -B -q package -DskipTests

# ---- Runtime stage ----
FROM eclipse-temurin:17-jre
WORKDIR /app
ENV TZ=Asia/Kuala_Lumpur \
    USER_DATA_FILE=/app/data/user_data.txt
COPY --from=build /src/target/waktusolatbot.jar /app/waktusolatbot.jar
VOLUME /app/data
ENTRYPOINT ["java", "-jar", "/app/waktusolatbot.jar"]
