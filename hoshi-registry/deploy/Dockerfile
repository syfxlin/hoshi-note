FROM maven:3-openjdk-17 as builder

WORKDIR /app
COPY . .

RUN mvn package -DskipTests -pl me.ixk.hoshi:hoshi-registry -am

FROM openjdk:17-jdk-alpine

WORKDIR /app
COPY --from=builder /app/hoshi-registry/target/hoshi-registry-1.0-SNAPSHOT.jar ./

CMD ["java", "-jar", "/app/hoshi-registry-1.0-SNAPSHOT.jar"]
