# Используем образ Maven для сборки
FROM maven:3.8.5-openjdk-17 AS build
WORKDIR /app

# Копируем только pom.xml и загружаем зависимости
COPY pom.xml ./email-sender/
RUN mvn -f ./email-sender dependency:go-offline

# Копируем исходный код
COPY src ./email-sender/src

# Собираем проект
RUN mvn -f ./email-sender clean package -DskipTests

# Используем более легкий образ для выполнения
FROM openjdk:17-jdk-alpine
WORKDIR /app

# Копируем собранный JAR
COPY --from=build /app/email-sender/target/*.jar ./email-sender.jar

# Запускаем приложение
ENTRYPOINT ["java", "-jar", "email-sender.jar"]
