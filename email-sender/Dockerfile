# Используем образ Maven для сборки
FROM maven:3.9.9-eclipse-temurin-17-alpine AS build
WORKDIR /app

# Копируем только pom.xml и загружаем зависимости
COPY pom.xml ./email-sender/
#RUN mvn -f ./email-sender dependency:go-offline

# Копируем исходный код
COPY src ./email-sender/src

# Собираем проект
#RUN mvn -f ./email-sender clean package -DskipTests
RUN --mount=type=cache,target=/root/.m2 mvn clean package -Dmaven.test.skip -f email-sender

# Используем более легкий образ для выполнения
FROM eclipse-temurin:17-jdk-alpine AS builder
WORKDIR /app

# Копируем собранный JAR
COPY --from=build /app/email-sender/target/*.jar ./email-sender.jar
RUN java -Djarmode=layertools -jar email-sender.jar extract

FROM eclipse-temurin:17-alpine
WORKDIR /app
COPY --from=builder app/dependencies/ ./
COPY --from=builder app/spring-boot-loader/ ./
COPY --from=builder app/snapshot-dependencies/ ./
COPY --from=builder app/application/ ./
# Запускаем приложение
ENTRYPOINT ["java", "org.springframework.boot.loader.launch.JarLauncher"]
