FROM eclipse-temurin:21
COPY build/libs/untangle-your-spaghetti-test-code.jar application.jar
ENTRYPOINT ["java", "-jar", "application.jar"]
