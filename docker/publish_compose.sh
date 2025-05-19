#!/bin/bash

# CONFIGURATION
DOCKERHUB_USER=sonaca
APP_NAME=codehub
VERSION=1.0.0
FILE=docker-compose.prod.yml
IMAGE_NAME=$DOCKERHUB_USER/$APP_NAME-compose
TAG=$IMAGE_NAME:$VERSION

# Check Docker and Compose
if ! command -v docker >/dev/null 2>&1 || ! docker compose version >/dev/null 2>&1; then
  echo "Error: Docker Compose no está instalado o no es accesible."
  exit 1
fi

# Check if docker compose supports publish
if ! docker compose --help | grep -q "publish"; then
  echo "Error: Tu versión de Docker Compose no soporta 'publish'. Actualiza Docker Desktop a v4.26 o superior."
  exit 1
fi

# Check file exists
if [ ! -f "$FILE" ]; then
  echo "Error: No se encuentra el archivo $FILE."
  exit 1
fi

# Login to Docker
echo "Iniciando sesión en Docker Hub..."
if ! docker login; then
  echo "Error: No se pudo iniciar sesión en Docker Hub."
  exit 1
fi

# Publicar OCI Artifact
echo "Publicando como OCI Artifact..."
if ! docker compose -f "$FILE" publish "$TAG" --with-env; then
  echo "Error: Fallo al publicar el OCI Artifact."
  exit 1
fi

echo " OCI Artifact publicado: $TAG"
