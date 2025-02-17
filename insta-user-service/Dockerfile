FROM openjdk:24-slim-bullseye
LABEL authors="jeremylee"

# set working directory inside the container
WORKDIR /app

# copy the compiled JAR into the application
COPY ./target/insta-user-service-0.0.1-SNAPSHOT.jar /app

EXPOSE 8001

CMD ["java", "-Dspring.profiles.active=docker", "-jar", "insta-user-service-0.0.1-SNAPSHOT.jar"]