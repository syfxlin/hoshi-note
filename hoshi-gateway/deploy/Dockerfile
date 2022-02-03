FROM maven:3-openjdk-17 as builder

WORKDIR /app
COPY . .

RUN mvn package -DskipTest -pl me.ixk.hoshi:hoshi-gateway -am

FROM openjdk:17-jdk-alpine

WORKDIR /app
COPY --from=builder /app/hoshi-gateway/target/hoshi-gateway-1.0-SNAPSHOT.jar ./

CMD ["java", "-jar", "/app/hoshi-gateway-1.0-SNAPSHOT.jar"]
