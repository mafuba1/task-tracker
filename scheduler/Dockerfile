# Используем образ Maven для сборки
FROM maven:3.9.9-eclipse-temurin-17-alpine AS build
WORKDIR /app

# Копируем только pom.xml и загружаем зависимости
COPY pom.xml ./scheduler/
#RUN mvn -f ./scheduler dependency:go-offline

# Копируем исходный код
COPY src ./scheduler/src

# Собираем проект
#RUN mvn -f ./scheduler clean package -DskipTests
RUN --mount=type=cache,target=/root/.m2 mvn clean package -Dmaven.test.skip -f scheduler

# Используем более легкий образ для выполнения
FROM eclipse-temurin:17-jdk-alpine AS builder
WORKDIR /app

# Копируем собранный JAR
COPY --from=build /app/scheduler/target/*.jar ./scheduler.jar
RUN java -Djarmode=layertools -jar scheduler.jar extract

FROM eclipse-temurin:17-alpine
WORKDIR /app
COPY --from=builder app/dependencies/ ./
COPY --from=builder app/spring-boot-loader/ ./
COPY --from=builder app/snapshot-dependencies/ ./
COPY --from=builder app/application/ ./
# Запускаем приложение
ENTRYPOINT ["java", "org.springframework.boot.loader.launch.JarLauncher"]
