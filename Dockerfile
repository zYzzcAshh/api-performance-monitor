# ---- Build stage ----
FROM eclipse-temurin:17-jdk-jammy AS build
WORKDIR /app

# Copy gradle files first for caching
COPY gradlew .
COPY gradle gradle
COPY build.gradle.kts settings.gradle.kts ./
COPY gradle.properties* ./

# Copy your actual source
COPY . .

RUN chmod +x gradlew
RUN ./gradlew :server:build -x test -x ktlintCheck -x ktlintTestSourceSetCheck -x ktlintMainSourceSetCheck --no-daemon
# ^ adjust the module name/task to whatever produces your runnable jar

# ---- Run stage ----
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app

# Copy the built jar from the build stage — adjust path to match your project
COPY --from=build /app/server/build/libs/*-all.jar app.jar

EXPOSE 8080
CMD ["java", "-jar", "app.jar"]