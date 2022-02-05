FROM maven:3-openjdk-17 as builder

WORKDIR /app
COPY . .

RUN --mount=type=cache,target=/root/.m2 mvn package -DskipTests

FROM openjdk:17-jdk-alpine

WORKDIR /app

COPY --from=builder /app/hoshi-gateway/target/hoshi-gateway-1.0-SNAPSHOT.jar ./
COPY --from=builder /app/hoshi-services/hoshi-file/target/hoshi-file-1.0-SNAPSHOT.jar ./
COPY --from=builder /app/hoshi-services/hoshi-mail/target/hoshi-mail-1.0-SNAPSHOT.jar ./
COPY --from=builder /app/hoshi-services/hoshi-note/target/hoshi-note-1.0-SNAPSHOT.jar ./
COPY --from=builder /app/hoshi-services/hoshi-ums/target/hoshi-ums-1.0-SNAPSHOT.jar ./
