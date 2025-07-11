# Use official Kotlin JDK image
FROM openjdk:21-jdk-slim

# Copy the fat jar (assuming you've built a shadow/fat jar)
COPY build/libs/StuddyBuddyDB-all.jar /app/app.jar

WORKDIR /app

EXPOSE 5431

ENTRYPOINT ["java", "-jar", "app.jar"]