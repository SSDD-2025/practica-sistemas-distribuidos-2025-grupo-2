#!/bin/bash

#First option but not recommended
#sudo apt list --installed 2>/dev/null| grep -m1 "docker"



if ! command -v docker >/dev/null 2>&1; 
then
    echo "Docker is not installed. Please install Docker first."
    exit 1
fi

if [ ! -f "Dockerfile" ]; 
then
    echo "Error: Dockerfile not found in the current directory."
    exit 1
fi

if ! docker build -t sonaca/codehub:latest -f Dockerfile .; then
    echo "Error: Failed to build Docker image."
    exit 1
fi


#Variable to verify that the image has been created
export SONACA_IMAGE_CREATED="true"

echo "Docker image sonaca/codehub:latest built successfully."

