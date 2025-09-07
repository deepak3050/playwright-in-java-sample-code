# Use OpenJDK 23 slim as the base image
FROM openjdk:23-slim

# Install dependencies
RUN apt-get update && \
    apt-get install -y curl zip unzip && \
    rm -rf /var/lib/apt/lists/*

# Install SDKMAN! and Maven 3.9.9
RUN curl -s "https://get.sdkman.io" | bash && \
    bash -c "source $HOME/.sdkman/bin/sdkman-init.sh && \
             sdk install maven 3.9.9"

# Set Maven environment variables
ENV MAVEN_HOME /root/.sdkman/candidates/maven/current
ENV PATH "$MAVEN_HOME/bin:$PATH"

# Verify Maven installation
RUN mvn -version

# Update CA certificates
RUN apt-get update && \
    apt-get install -y --no-install-recommends ca-certificates && \
    rm -rf /var/lib/apt/lists/*

# Install Node.js and npm
RUN curl -fsSL https://deb.nodesource.com/setup_18.x | bash - && \
    apt-get install -y nodejs && \
    rm -rf /var/lib/apt/lists/*

# Install Playwright dependencies
RUN npx playwright install-deps

# Test internet connectivity
RUN curl -I https://repo.maven.apache.org/maven2/

# Set the working directory inside the container
WORKDIR /tests

# Copy the Maven project files to the container
COPY . .

# Run Maven to execute the tests
CMD ["mvn", "verify"]
