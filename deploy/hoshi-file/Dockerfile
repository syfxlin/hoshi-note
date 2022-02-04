FROM maven:3-openjdk-17 as builder

WORKDIR /app
COPY . .

RUN --mount=type=cache,target=/root/.m2 mvn package -DskipTests -pl me.ixk.hoshi:hoshi-file -am

FROM openjdk:17-jdk-alpine

WORKDIR /app
COPY --from=builder /app/hoshi-services/hoshi-file/target/hoshi-file-1.0-SNAPSHOT.jar ./

CMD ["java", "-jar", "/app/hoshi-file-1.0-SNAPSHOT.jar"]
