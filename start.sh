#!/bin/bash

# Determine which service to run based on SERVICE_NAME environment variable
# Default to api-gateway if not specified
SERVICE_NAME=${SERVICE_NAME:-api-gateway}

echo "Starting service: $SERVICE_NAME"

# Compile the specific module
echo "Building $SERVICE_NAME..."
mvn clean package -DskipTests -pl $SERVICE_NAME -am

# Check if build was successful
if [ $? -ne 0 ]; then
    echo "Build failed for $SERVICE_NAME"
    exit 1
fi

# Find and run the JAR file
JAR_FILE=$(find $SERVICE_NAME/target -name "*.jar" -type f | head -n 1)

if [ -z "$JAR_FILE" ]; then
    echo "No JAR file found in $SERVICE_NAME/target"
    exit 1
fi

echo "Found JAR: $JAR_FILE"
echo "Running application..."

# Run the JAR with the PORT environment variable
java -jar $JAR_FILE
