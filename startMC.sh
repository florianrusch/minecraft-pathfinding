#!/usr/bin/env sh

set -e

./gradlew clean build copyArtifactToDockerServer

cd .data/
java -XX:+UseG1GC -Xms2G -Xmx2G -Xdebug -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5005 -jar craftbukkit_server.jar
