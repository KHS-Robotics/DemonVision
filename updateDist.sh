#!/bin/sh

# Updates dist/DemonVision.jar
sudo rm -r dist/ build
sudo gradle jar
mkdir dist
cp build/libs/DemonVision.jar dist/