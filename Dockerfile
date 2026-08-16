FROM eclipse-temurin:25-jre-alpine
COPY target/student-management-0.0.1-SNAPSHOT.jar /app/student-management.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/student-management.jar"]
