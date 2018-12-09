FROM openjdk:11-jre

EXPOSE 8080

COPY build/libs/*.jar application.jar

CMD java -jar application.jar