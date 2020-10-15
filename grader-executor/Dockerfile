FROM openjdk:11-jre-slim
ARG BUILD_VERSION
ADD grader-executor-${BUILD_VERSION}.tar .
RUN mv grader-executor-${BUILD_VERSION} grader-executor
ENTRYPOINT grader-executor/bin/grader-executor
