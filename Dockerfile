# Use an official OpenJDK runtime as a parent image
FROM openjdk:20-slim

# Set the working directory
WORKDIR /app

# Copy the entire project (including pom.xml and core/)
COPY . .

# Build the project with Maven
RUN mvn clean package -f core/pom.xml

# Expose the port your app uses (usually 8080)
EXPOSE 8080

# Define the command to run your app
CMD ["java", "-jar", "core/target/core-1.0-SNAPSHOT.jar"]