FROM openjdk:17
EXPOSE 9292
ADD target/spring-reactive-mongo-crud.jar spring-reactive-mongo-crud.jar
ENTRYPOINT ["java", "-jar","/spring-reactive-mongo-crud.jar"]