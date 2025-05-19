#!/bin/bash

# Check if Docker is installed
if ! command -v docker >/dev/null 2>&1; then
    echo "Docker is not installed. Please install Docker first."
    exit 1
fi

# Check if the image exists
if ! docker image inspect sonaca/codehub:latest >/dev/null 2>&1; then
    echo "Error: The image sonaca/codehub:latest does not exist. Please run create_image.sh first."
    exit 1
fi

# Log in to Docker Hub
if ! docker login; then
    echo "Error: Failed to log in to Docker registry."
    exit 1
fi

# Push the image to Docker Hub
if ! docker push sonaca/codehub:latest; then
    echo "Error: Failed to push Docker image."
    exit 1
fi

echo "Docker image sonaca/codehub:latest pushed successfully."