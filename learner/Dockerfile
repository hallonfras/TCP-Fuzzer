FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app
RUN git clone https://github.com/protocol-fuzzing/protocol-state-fuzzer.git psf \
    && \
    cd psf && bash install.sh && cd ../..
COPY pom.xml .
COPY src ./src
RUN mvn clean package




FROM bitnami/java:17
WORKDIR /app
COPY --from=build /app/target/TCP-Learner.jar ./TCP-Learner.jar
COPY --from=build /app/target/lib/ ./lib/

ENTRYPOINT ["java", "-jar", "TCP-Learner.jar"]
