FROM eclipse-temurin:21-jdk-jammy as builder

WORKDIR /app

COPY . .

RUN chmod +x ./gradlew

RUN ./gradlew build -x test -x validateStructure

FROM eclipse-temurin:21-jre-jammy

WORKDIR /app

COPY --from=builder /app/applications/app-service/build/libs/*.jar ./app.jar

EXPOSE 8081

ENTRYPOINT ["java", "-jar", "app.jar"]