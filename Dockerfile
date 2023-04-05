FROM java:8
MAINTAINER stukk
ADD stukk-0.0.1-SNAPSHOT.jar stukk.jar
ENTRYPOINT ["java","-jar","stukk.jar"]
