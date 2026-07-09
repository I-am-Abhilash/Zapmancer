FROM eclipse-temurin:21-jre

WORKDIR /app

COPY server/build/install/server/ ./

EXPOSE 8080

ENTRYPOINT ["bin/server"]