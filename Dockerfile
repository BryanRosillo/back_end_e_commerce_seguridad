FROM maven:maven:3.8-openjdk-17-slim AS build
WORKDIR /app
COPY pom.xml ./
COPY src ./src

ARG JWT_SECRETO
ENV JWT_SECRETO=${JWT_SECRETO}

RUN mvc clean package

FROM maven:maven:3.8-openjdk-17-slim
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT [ "java", "-jar", "app.jar" ]