FROM openjdk:24-slim-bullseye
LABEL authors="jeremylee"

# set working directory inside the container
WORKDIR /app

# copy the compiled JAR into the application
COPY ./target/insta-service-registry-0.0.1-SNAPSHOT.jar /app

EXPOSE 8761

CMD ["java", "-jar", "insta-service-registry-0.0.1-SNAPSHOT.jar"]