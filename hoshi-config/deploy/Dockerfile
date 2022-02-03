FROM maven:3-openjdk-17 as builder

WORKDIR /app
COPY . .

RUN mvn package -DskipTest -pl me.ixk.hoshi:hoshi-config -am

FROM openjdk:17-jdk-alpine

WORKDIR /app
COPY --from=builder /app/hoshi-config/target/hoshi-config-1.0-SNAPSHOT.jar ./

CMD ["java", "-jar", "/app/hoshi-config-1.0-SNAPSHOT.jar"]
