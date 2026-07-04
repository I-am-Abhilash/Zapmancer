FROM eclipse-temurin:21-jre

WORKDIR /app

COPY server/app/build/install/server-app/ ./

EXPOSE 8080

ENTRYPOINT ["bin/server-app"]