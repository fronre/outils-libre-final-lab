# Multi-stage build for Java Pricing Engine
# Stage 1: Build
FROM eclipse-temurin:23-jdk AS builder

WORKDIR /build

# Copy build files
COPY build.gradle settings.gradle ./
COPY gradle/ ./gradle/
COPY src/ ./src/

# Build with javac
RUN mkdir -p build/classes && \
    javac -d build/classes src/main/java/com/pricing/*.java

# Stage 2: Runtime
FROM eclipse-temurin:23-jre-alpine

WORKDIR /app

# Copy compiled classes from builder
COPY --from=builder /build/build/classes /app/classes

# Set classpath and run
ENV CLASSPATH=/app/classes

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=5s --retries=3 \
    CMD java -version || exit 1

# Entry point
ENTRYPOINT ["java", "-cp", "/app/classes", "com.pricing.PricingEngine"]
