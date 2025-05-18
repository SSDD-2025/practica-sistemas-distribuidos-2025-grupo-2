#!/bin/bash

# CONFIGURATION
DOCKERHUB_USER=sonaca
APP_NAME=codehub
VERSION=1.0.0
FILE=docker-compose.prod.yml
TAG=$DOCKERHUB_USER/$APP_NAME-compose:$VERSION

# Check if the file exists
if [ ! -f "$FILE" ]; then
  echo "The file $FILE does not exist."
  exit 1
fi

# Create an OCI artifact (compressed tar format)
tar -czf $FILE.tgz $FILE

# Push the artifact to Docker Hub
oras push $TAG \
  --artifact-type application/vnd.docker.distribution.manifest.v2+json \
  --media-type application/x-yaml \
  $FILE.tgz

echo "OCI Artifact published as $TAG"
