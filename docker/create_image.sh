#!/bin/bash

if ! command -v docker >/dev/null 2>&1; then
    echo "Docker is not installed. Please install Docker first."
    exit 1
fi

DOCKERFILE_PATH="docker/Dockerfile"

if [ ! -f "$DOCKERFILE_PATH" ]; then
    echo "Error: Dockerfile not found at $DOCKERFILE_PATH"
    exit 1
fi

if ! docker build -t sonaca/codehub:latest -f "$DOCKERFILE_PATH" .; then
    echo "Error: Failed to build Docker image."
    exit 1
fi

echo "Docker image sonaca/codehub:latest built successfully."
