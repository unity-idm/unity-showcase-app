#!/bin/bash

pushd `dirname $0` > /dev/null
BASE_DIR=`pwd`
popd > /dev/null
BASE_DIR=`dirname "$BASE_DIR"` 


. "${BASE_DIR}"/conf/startup.properties


if [ -e "$PID" ]
then
	kill `cat "$PID"`
else
	echo "No $PID file. It seems that server is not running."
	exit
fi

P=`cat "$PID"`
echo "Waiting for server to stop..."
stopped="no"
until [ "$stopped" = "" ]; do
  stopped=$(ps -p $P | grep $P)
  if [ $? != 0 ]
  then
    stopped=""
  fi
  sleep 1
done

echo "Server stopped."

rm -f "$PID"