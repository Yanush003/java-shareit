FROM amazoncorretto:11
# Рабочая директория
WORKDIR /app

# Копируем JAR файл в контейнер
COPY target/shareit-server-0.0.1-SNAPSHOT.jar /app/shareIt-server.jar

# Запускаем приложение
CMD ["java", "-jar", "shareIt-server.jar"]