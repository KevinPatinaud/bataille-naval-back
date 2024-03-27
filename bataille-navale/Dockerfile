



# Choisir l'image de base
FROM openjdk:17-oracle AS build

# Définir le répertoire de travail
WORKDIR /app

# Copier les fichiers de build (gradle/maven)
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
COPY bataille-api              bataille-api
COPY bataille-communication    bataille-communication
COPY bataille-engine           bataille-engine
COPY bataille-model            bataille-model
COPY bataille-navale.iml       bataille-navale.iml
COPY bataille-persistence      bataille-persistence
COPY bataille-player           bataille-player


# Construire l'application
RUN ./mvnw package

# Étape de l'image finale
FROM openjdk:17-oracle
COPY --from=build /app/bataille-api/target/*.jar bataille.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/bataille.jar"]