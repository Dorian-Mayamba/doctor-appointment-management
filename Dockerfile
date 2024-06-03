FROM openjdk:17
ARG JAR_FILE=build/libs/doctor-1.0.2.jar
COPY ${JAR_FILE} app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app.jar"]