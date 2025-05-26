FROM openjdk:21-jdk-slim

WORKDIR /app

COPY build.gradle settings.gradle gradlew ./
COPY gradle ./gradle


RUN ./gradlew dependencies --no-daemon || true


COPY . .


RUN ./gradlew build -x test --no-daemon


CMD java -jar $(find build/libs -name "*.jar" | head -n 1)