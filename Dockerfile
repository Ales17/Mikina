FROM amazoncorretto:20
COPY *.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.jar"]