FROM openjdk:24-slim-bullseye
LABEL authors="jeremylee"

# set working directory inside the container
WORKDIR /app

# copy the compiled JAR into the application
COPY ./target/insta-config-server-0.0.1-SNAPSHOT.jar /app

EXPOSE 8888

CMD ["java", "-jar", "insta-config-server-0.0.1-SNAPSHOT.jar"]