#!/bin/bash

# Check if variable its true
if [ "$SONACA_IMAGE_CREATED" != "true" ]; 
then
    echo "Error: The image has not been created. Please run create_image.sh first."
    exit 1
fi

if ! docker login; 
then
    echo "Error: Failed to log in to Docker registry."
    exit 1
fi

if ! docker push sonaca/codehub:latest; 
then
    echo "Error: Failed to push Docker image."
    exit 1
fi

echo "Docker image sonaca/codehub:latest pushed successfully."