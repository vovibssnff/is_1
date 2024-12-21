#!/bin/bash

# Create necessary directories in the destination
mkdir -p "${PWD}/backend/src/main/webapp/resources/css"
mkdir -p "${PWD}/backend/src/main/webapp/resources/img"
mkdir -p "${PWD}/backend/src/main/webapp/resources/js"

# Copy contents from the frontend/dist directory to the backend/webapp directory
cp -a "${PWD}/frontend/dist/css/." "${PWD}/backend/src/main/webapp/resources/css/"
cp -a "${PWD}/frontend/dist/img/." "${PWD}/backend/src/main/webapp/resources/img/"
cp -a "${PWD}/frontend/dist/js/." "${PWD}/backend/src/main/webapp/resources/js/"
cp -a "${PWD}/frontend/dist/favicon.ico" "${PWD}/backend/src/main/webapp/"
cp -a "${PWD}/frontend/dist/index.html" "${PWD}/backend/src/main/webapp/"
