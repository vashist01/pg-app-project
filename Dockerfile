FROM openjdk:17
ADD target/kunj.jar kunj.jar
EXPOSE 8005
ENTRYPOINT ["java","-jar","kunj.jar"]