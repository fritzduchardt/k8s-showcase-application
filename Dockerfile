# Creates Spring Boot Fat Jar
FROM openjdk:11
LABEL maintainer="fduchardt"
COPY . /k8sshowcase
WORKDIR /k8sshowcase
RUN ./gradlew bootJar
RUN mv ./build/libs/*.jar ./build/libs/k8sshowcase.jar

# Debian container with kubectl and Java
FROM openjdk:11
LABEL maintainer="fduchardt"
#COPY --from=0 /k8sshowcase/build/libs/k8sshowcase.jar ./
COPY build/libs/k8sshowcase-0.0.1.jar ./k8sshowcase.jar
CMD java -jar k8sshowcase.jar