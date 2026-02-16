#!/bin/sh


###############################################################################
#################### sh script to execute generator-bash ######################
###############################################################################


IGEN_VERSION=5.0.0-M1
IGEN_RUNNABLE_JAR="$IGEN_HOME/boot/generator-bash-$IGEN_VERSION.jar"
IGEN_LIB=$IGEN_HOME/lib
IGEN_CLASSPATH=$IGEN_RUNNABLE_JAR:$IGEN_LIB/*

RUNNABLE_CLASS="org.ifolks.generator.bash.launcher.MainLauncher"

echo current directory : $PWD
echo generator home : $IGEN_HOME
echo version : $IGEN_VERSION
echo java home : $JAVA_HOME

IGEN_CMD_LINE_ARGS=$1
DATABASE=$2

RUN(){
  echo "start IGEN"
  echo "to exec: $JAVA_HOME/bin/java -classpath $IGEN_CLASSPATH $RUNNABLE_CLASS $IGEN_CMD_LINE_ARGS $PWD $DATABASE"
  eval "$JAVA_HOME/bin/java -classpath $IGEN_CLASSPATH $RUNNABLE_CLASS $IGEN_CMD_LINE_ARGS $PWD $DATABASE"
  echo "end IGEN"
  END
}



ERROR(){
  echo "FAILED"
  exit 1
}

END(){
  echo "END"
}

# ==== START VALIDATION ====
if [ ! -z "$JAVA_HOME" ]; then

  if [ -f "$JAVA_HOME/bin/java" ]; then

    if [ ! -z "$IGEN_HOME" ]; then

      if [ -f "$IGEN_HOME/bin/IGEN.sh" ]; then
        RUN
      else
        echo "ERROR: IGEN_HOME is set to an invalid directory."
        echo "IGEN_HOME = $IGEN_HOME"
        echo "Please set the IGEN_HOME variable in your environment to match the"
        echo "location of your ifolks generator installation"
        ERROR
      fi

    else
      echo "ERROR: IGEN_HOME not found in your environment."
      echo "Please set the IGEN_HOME variable in your environment to match the"
      echo "location of the ifolks generator installation"
      ERROR
    fi

  else
    echo "ERROR: JAVA_HOME is set to an invalid directory."
    echo "JAVA_HOME = $JAVA_HOME"
    echo "Please set the JAVA_HOME variable in your environment to match the"
    echo "location of your Java installation"
    ERROR
  fi

else
  echo "ERROR: JAVA_HOME not found in your environment."
  echo "Please set the JAVA_HOME variable in your environment to match the"
  echo "location of your Java installation"
  ERROR
fi
