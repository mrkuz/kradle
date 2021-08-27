#!/bin/sh -e
if [ "${JAVA_URANDOM:-true}" = "true" ]; then
    JAVA_OPTS="-Djava.security.egd=file:/dev/./urandom $JAVA_OPTS"
fi
if [ "$JAVA_DIAGNOSTICS" = "true" ]; then
    JAVA_OPTS="-XX:+UnlockDiagnosticVMOptions -XX:+PrintFlagsFinal -Xlog:gc $JAVA_OPTS"
fi
if [ "$JAVA_DEBUG" = "true" ]; then
    JAVA_OPTS="-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:${JAVA_DEBUG_PORT:-8000} $JAVA_OPTS"
fi
if [ -n "$JAVA_AGENT" ]; then
    JAVA_OPTS="-agentpath:$JAVA_AGENT $JAVA_OPTS"
fi
JAVA_OPTS="-XX:+ExitOnOutOfMemoryError $JAVA_OPTS"

echo exec java $JAVA_OPTS -cp @/app/jib-classpath-file "$MAIN_CLASS" "$@"
exec java $JAVA_OPTS -cp @/app/jib-classpath-file "$MAIN_CLASS" "$@"
