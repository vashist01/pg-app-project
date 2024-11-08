FROM openjdk:17
ADD target/pg-app-project.jar pg-app-project.jar
EXPOSE 8005
  ENTRYPOINT ["java","-jar","pg-app-project.jar"]