#!/bin/bash

# E2E Tests Runner Script
# Starts the application, runs Newman tests, and stops the application

set -e

APP_PORT=8080
APP_PID=""

cleanup() {
    echo "Stopping application..."
    if [ -n "$APP_PID" ]; then
        kill $APP_PID 2>/dev/null || true
        wait $APP_PID 2>/dev/null || true
    fi
}

trap cleanup EXIT

# Check if Newman is installed
if ! command -v newman &> /dev/null; then
    echo "Newman is not installed. Install it with: npm install -g newman"
    exit 1
fi

# Check if port is already in use
if lsof -i :$APP_PORT &> /dev/null; then
    echo "Port $APP_PORT is already in use. Stop any running application first."
    exit 1
fi

echo "Starting application..."
./gradlew bootRun &
APP_PID=$!

# Wait for application to start
echo "Waiting for application to be ready..."
for i in {1..30}; do
    if curl -s http://localhost:$APP_PORT/v3/api-docs &> /dev/null; then
        echo "Application is ready!"
        break
    fi
    if [ $i -eq 30 ]; then
        echo "Application failed to start within 30 seconds"
        exit 1
    fi
    sleep 1
done

echo "Running E2E tests..."
newman run postman/Promotions_API.postman_collection.json

echo "E2E tests completed successfully!"
