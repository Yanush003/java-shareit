FROM amazoncorretto:11

# Рабочая директория
WORKDIR /app

# Копируем JAR файл в контейнер
COPY target/shareit-gateway-0.0.1-SNAPSHOT.jar /app/shareIt-gateway.jar


# Запускаем приложение
CMD ["java", "-jar", "shareIt-gateway.jar"]
