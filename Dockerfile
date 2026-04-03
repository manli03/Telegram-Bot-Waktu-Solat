# Use a maintained multi-arch OpenJDK runtime (Temurin) as a base image
# Temurin images are multi-arch and commonly available for both amd64 and arm64
FROM eclipse-temurin:8-jre

# Set the working directory in the Docker container
WORKDIR /app

# Set the environment variable for the timezone
ENV TZ=Asia/Kuala_Lumpur

# Copy the JAR file created by the Maven build into the Docker image
# Make sure this JAR file is the 'jar-with-dependencies'
COPY target/Template-MyClass-1.0-SNAPSHOT-jar-with-dependencies.jar /app/telegrambot.jar

# Set the command to run the JAR file
ENTRYPOINT ["java", "-jar", "/app/telegrambot.jar"]

