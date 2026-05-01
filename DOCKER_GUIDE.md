# Docker Setup Guide

## Overview
This project includes Docker containerization for easy deployment and testing.

## Files
- **Dockerfile** - Multi-stage build for production (JDK → JRE)
- **Dockerfile.test** - Test runner container
- **docker-compose.yml** - Orchestration for app + tests
- **.dockerignore** - Exclude unnecessary files from build

## Build & Run

### Option 1: Docker Compose (Recommended)
```bash
# Build and start all services
docker-compose up --build

# Run only the app
docker-compose up pricing-engine

# Run tests
docker-compose up pricing-tests

# Stop services
docker-compose down
```

### Option 2: Manual Docker
```bash
# Build image
docker build -t pricing-engine:latest .

# Run container
docker run -it pricing-engine:latest

# Check logs
docker logs <container-id>
```

## Key Features

### Multi-Stage Build
- **Stage 1**: Full JDK (eclipse-temurin:23-jdk)
  - Compiles source code
  - Builds classes
  
- **Stage 2**: Slim JRE (eclipse-temurin:23-jre-alpine)
  - Runs compiled classes only
  - Smaller image size (~100MB vs 400MB+)

### Health Checks
```yaml
healthcheck:
  interval: 30s
  timeout: 3s
  retries: 3
  start_period: 5s
```

### Environment Variables
```bash
JAVA_OPTS: "-Xmx256m -Xms128m"  # Memory limits
TEST_TIMEOUT: "60"               # Test timeout
```

## Image Sizes
- **Builder stage**: ~400MB (with JDK)
- **Final image**: ~100MB (JRE only)
- **Compression**: ~60-70% smaller

## Example Commands

```bash
# Build and run with compose
docker-compose up --build

# Run specific service
docker-compose up pricing-engine

# View container logs
docker-compose logs -f pricing-engine

# Execute command in running container
docker-compose exec pricing-engine java -version

# Clean up
docker-compose down -v
```

## Troubleshooting

### Container exits immediately
```bash
docker logs <container-id>
```

### Port already in use
```bash
docker-compose down
# or change port in docker-compose.yml
```

### Rebuild without cache
```bash
docker-compose build --no-cache
```

## Next Steps
1. Install Docker Desktop
2. Run: `docker-compose up --build`
3. View logs: `docker-compose logs -f`
4. Run tests: `docker-compose up pricing-tests`
