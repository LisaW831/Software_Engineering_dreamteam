FROM openjdk:latest
COPY ./target/seMethods-0.1.0.2(0.1-alpha-2)-jar-with-dependencies.jar /tmp
WORKDIR /tmp
ENTRYPOINT ["java", "-jar", "seMethods-0.1.0.2(0.1-alpha-2)-jar-with-dependencies.jar", "db:3306", "10000"]