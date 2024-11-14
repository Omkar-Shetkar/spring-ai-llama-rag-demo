# Use the Ollama base image
FROM ollama/ollama

# Copy the wait-for-ollama script into the container
COPY wait-for-ollama.sh /usr/local/bin/wait-for-ollama.sh

# Make it executable
RUN chmod +x /usr/local/bin/wait-for-ollama.sh

# Set the entrypoint to run the wait-for-ollama script
ENTRYPOINT ["/usr/local/bin/wait-for-ollama.sh"]