FROM openjdk:11-jre-slim

WORKDIR /home/ubuntu/

COPY /build/libs/imhero-0.0.1-SNAPSHOT.jar .

#CMD java -jar -javaagent:/pinpoint/pinpoint-bootstrap-2.2.3-NCP-RC1.jar -Dpinpoint.applicationName=IMHERO -Dpinpoint.agentId=IMHERO-agent imhero-0.0.1-SNAPSHOT.jar

CMD java -jar imhero-0.0.1-SNAPSHOT.jar
