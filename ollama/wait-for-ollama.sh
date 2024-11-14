#!/bin/bash

# Wait for the Ollama service to be available
until $(curl --output /dev/null --silent --head --fail http://ollama:11434); do
    echo "Waiting for Ollama to be ready..."
    sleep 5
done

echo "Ollama is ready!"

# Now you can pull models and serve them
ollama pull nomic-embed-text:latest
ollama pull llama3.2:latest
ollama serve