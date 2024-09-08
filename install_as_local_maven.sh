#!/bin/bash

# Install the jar to ~/.m2 as a maven package
# muquit@muquit.com Sep-08-2024 

MYDIR=$(dirname $0)
JAR_FILE="${MYDIR}/target/gpw-1.0.1.jar"
GROUP_ID="com.muquit.gpw"
ARTIFACT_ID="gpw"
VERSION="1.0.1"

# local maven repo
LOCAL_REPO="${HOME}/.m2/repository/com/muquit/gpw"

cleanup() {
    /bin/rm -f ${JAR_FILE}
    if [[ -d ${LOCAL_REPO} ]]; then
        echo "Removing local repo ..."
        /bin/rm -rf ${LOCAL_REPO}
    fi
}

build() {
    mvn clean &> /dev/null
    echo "Compiling package ..."
    mvn package &>/dev/null
    if [[ $? != 0 ]]; then
        echo "ERROR: Could not build the jar package"
        exit 1
    fi
}


check() {
    if [[ ! -f ${JAR_FILE} ]]; then
        echo "ERROR: jar file ${JAR_FILE} not found. aborting ..."
        exit 1
    fi
}

install() {
    echo "Installing to ${LOCAL_REPO} ..."
    mvn install:install-file \
       -Dfile=${JAR_FILE} \
       -DgroupId=${GROUP_ID} \
       -DartifactId=${ARTIFACT_ID} \
       -Dversion=${VERSION} \
       -Dpackaging=jar \
       -DgeneratePom=true &>/dev/null
    if [[ $? != 0 ]]; then
        echo "ERROR: Could not install ${JAR_FILE} to ${LOCAL_REPO}"
        exit 1
    fi
    echo "Done"
}



####
    cleanup
    build
    check
    install
