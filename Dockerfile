# Use an official OpenJDK runtime as a base image
FROM openjdk:8-jre-slim

# Set the working directory in the Docker container
WORKDIR /app

# Set the environment variable for the timezone
ENV TZ=Asia/Kuala_Lumpur

# Copy the JAR file created by the Maven build into the Docker image
# Make sure this JAR file is the 'jar-with-dependencies'
COPY target/Template-MyClass-1.0-SNAPSHOT-jar-with-dependencies.jar /app/telegrambot.jar

# Set the command to run the JAR file
ENTRYPOINT ["java", "-jar", "/app/telegrambot.jar"]

