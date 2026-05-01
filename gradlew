#!/bin/bash

#
# Gradle start up script for UN*X
#

# Attempt to set APP_HOME
# Resolve links: $0 may be a link
PRG="$0"
# Need this for daisy-chained symlinks.
while
    APP_HOME=${PRG%"${PRG##*/}"}
    [ -h "$PRG" ]
do
    PRG=$(readlink "$PRG")
    APP_HOME=${PRG%"${PRG##*/}"}
done

# This is normally unused
APP_BASE_NAME=${0##*/}
APP_HOME=$(cd "${APP_HOME:-.}" && pwd -P) || exit

# Add default JVM options here
DEFAULT_JVM_OPTS='"-Xmx64m" "-Xms64m"'

# Use the maximum available, or set MAX_FD != -1 to use that value.
MAX_FD=maximum

warn() {
    echo "$*" >&2
}

die() {
    echo
    echo "$*"
    echo
    exit 1
}

# OS specific support (must be 'true' or 'false').
cygwin=false
msys=false
darwin=false
nonstop=false
case "$(uname)" in
CYGWIN*)
    cygwin=true
    ;;
Darwin*)
    darwin=true
    ;;
MSYS*)
    msys=true
    ;;
NONSTOP*)
    nonstop=true
    ;;
esac

CLASSPATH=$APP_HOME/gradle/wrapper/gradle-wrapper.jar

# Determine the Java command to use to start the JVM.
if [ -n "$JAVA_HOME" ]; then
    if [ -x "$JAVA_HOME/jre/sh/java" ]; then
        # IBM's JDK on AIX uses strange locations for the executables
        JAVACMD="$JAVA_HOME/jre/sh/java"
    else
        JAVACMD="$JAVA_HOME/bin/java"
    fi
else
    JAVACMD=java
fi

# Split the PATH into an array to check for java executable in PATH windows will put the PathExt dir in the back
PATH=(${PATH//:/ })

exec "$JAVACMD" $DEFAULT_JVM_OPTS $JAVA_OPTS $GRADLE_OPTS "-Dorg.gradle.appname=$APP_BASE_NAME" -classpath "$CLASSPATH" org.gradle.wrapper.GradleWrapperMain "$@"
