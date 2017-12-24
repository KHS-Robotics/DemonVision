#!/bin/sh

# Sets permissions on files for eclipse plugin
# For now, this has only been tested on Gradle 4.4,
# as it makes assumptions about the file structure

GRADLE_VERSION="4.4"

cd .gradle/$GRADLE_VERSION

cd fileChanges
sudo chmod a+rwx *
cd ..

cd fileContent
sudo chmod a+rwx *
cd ..

cd fileHashes
sudo chmod a+rwx *
cd ..

cd taskHistory
sudo chmod a+rwx *
cd ..

cd ../buildOutputCleanup
sudo chmod a+rwx *

cd ../..
