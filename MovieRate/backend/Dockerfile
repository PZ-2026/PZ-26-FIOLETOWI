# ============================================================
# Stage 1: Build the application with Gradle
# ============================================================
FROM gradle:8.14-jdk21 AS builder

WORKDIR /app

# Copy Gradle wrapper and build files first (for layer caching)
COPY gradlew gradlew.bat ./
COPY gradle ./gradle
COPY build.gradle settings.gradle ./

# Copy the local JAR dependency (pdf-reports)
COPY libs ./libs

# Download dependencies (cached layer)
RUN gradle dependencies --no-daemon || true

# Copy source code
COPY src ./src

# Build the application (skip tests for faster Docker builds)
RUN gradle bootJar --no-daemon -x test

# ============================================================
# Stage 2: Runtime image with JRE only
# ============================================================
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Install wget for health check
RUN apk add --no-cache wget

# Create the logs directory and a non-root user for security
RUN mkdir -p /app/logs && \
    addgroup -S appgroup && \
    adduser -S appuser -G appgroup && \
    chown -R appuser:appgroup /app

# Copy the built JAR from the builder stage
COPY --from=builder /app/build/libs/*.jar app.jar

# Switch to non-root user
USER appuser

# Expose the application port
EXPOSE 8080

# Health check — verify the app is listening on its port
HEALTHCHECK --interval=30s --timeout=3s --start-period=40s --retries=3 \
    CMD wget --no-verbose --tries=1 --spider http://localhost:8080/ 2>&1 | grep -qE "404|200|302" || exit 1

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
