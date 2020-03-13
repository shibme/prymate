FROM alpine
LABEL maintainer="shibme"
RUN apk add --no-cache openjdk8-jre
RUN mkdir prymate
COPY /target/prymate-proxy.jar /prymate/prymate-proxy.jar
WORKDIR workspace
CMD ["java", "-jar", "/prymate/prymate-proxy.jar"]