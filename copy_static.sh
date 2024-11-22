#!/bin/bash

COPY_PATH="${PWD}/backend/src/main/webapp/frontend"
cp -a "${PWD}/frontend/dist/." "$COPY_PATH"

