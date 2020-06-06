#!/bin/bash


pushd `dirname $0` > /dev/null
BASE_DIR=`pwd`
popd > /dev/null
BASE_DIR=`dirname "$BASE_DIR"` 


. "${BASE_DIR}"/conf/startup.properties



if [ -e "$PID" ]
 then
  if [ -d /proc/$(cat "$PID") ]
   then
     echo "A showcase app instance may be already running with process id "$(cat $PID)
     echo "If this is not the case, delete the file $PID and re-run this script"
     exit 1
   fi
fi


PWD="`pwd`"
cd "$BASE_DIR"

RUN_COMMAND="$JAVA ${MEM} ${OPTS}  -jar ${LIB}/${JAR}"
nohup ${RUN_COMMAND} >& "$STARTLOG" &

cd "$PWD"
 
echo $! > "$PID"