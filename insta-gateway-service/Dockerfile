FROM openjdk:24-slim-bullseye
LABEL authors="jeremylee"

# set working directory inside the container
WORKDIR /app

# copy the compiled JAR into the application
COPY ./target/insta-gateway-service-0.0.1-SNAPSHOT.jar /app

EXPOSE 8765

CMD ["java", "-jar", "insta-gateway-service-0.0.1-SNAPSHOT.jar"]