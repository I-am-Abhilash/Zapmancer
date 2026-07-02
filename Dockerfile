FROM eclipse-temurin:21-jre-jammy

# 1. Create a directory for the app
RUN mkdir /app

# 2. Expose the port the app runs on
EXPOSE 8080

# 3. Copy the fat JAR built by the previous step (Cloud Build or local Gradle)
# Note: This assumes you run './gradlew :app:buildFatJar' before 'docker build'
COPY server/app/build/libs/*.jar /app/ktor-app.jar

# 4. Run the application
ENTRYPOINT ["java", "-jar", "/app/ktor-app.jar"]
