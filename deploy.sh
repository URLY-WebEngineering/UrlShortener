#!/bin/bash

# Output Colors
COLOR_OFF='\033[0m'       # Text Reset
RED='\033[0;31m'          # Red
GREEN='\033[0;32m'        # Green

# Build and Test
echo "Building and Testing ... "
./gradlew build

if [ $? -ne 0 ]; then
  echo -e "Test: ${RED}FAILED${COLOR_OFF}"
  exit 1
else
  echo -e "Test: ${GREEN}PASSED${COLOR_OFF}"
fi

# Execute
docker-compose up