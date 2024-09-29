#!/bin/bash

VERSION=1.1.4
APP_NAME="TavChart"
JAR_NAME="tav-chart-${VERSION}-full.jar"
BUILD_DIR="${HOME}/IdeaProjects/TAvChart/target/"

"${JAVA_HOME}/bin/jpackage" -t app-image \
--app-version ${VERSION} \
--name ${APP_NAME} \
--verbose --input "${BUILD_DIR}" \
--main-jar "${BUILD_DIR}/${JAR_NAME}" \
--dest "${HOME}"