#!/bin/bash
#
# Copyright (C) 2018 Red Hat, Inc. (jdcasey@commonjava.org)
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#         http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#

THIS=$(cd ${0%/*} && echo $PWD/${0##*/})

BASEDIR=`dirname ${THIS}`
BASEDIR=`dirname ${BASEDIR}`

AQ_CONF_DIR=${AQ_CONF_DIR:-${BASEDIR}/conf}

echo "Loading extra config from classpath directory: ${AQ_CONF_DIR}"

CP="${AQ_CONF_DIR}"

for f in $(find $BASEDIR/lib/*.jar -type f)
do
  CP=${CP}:${f}
done

for f in $(find $BASEDIR/lib/thirdparty -type f)
do
  CP=${CP}:${f}
done

# echo "JAVA classpath:  ${CP}"

JAVA=`which java`
$JAVA -version 2>&1 > /dev/null
if [ $? != 0 ]; then
  PATH=${JAVA_HOME}/bin:${PATH}
  JAVA=${JAVA_HOME}/bin/java
fi

JAVA_OPTS="$JAVA_OPTS $JAVA_DEBUG_OPTS"

MAIN_CLASS=org.commonjava.auditquery.boot.Main

"$JAVA" ${JAVA_OPTS} -cp "${CP}" -Dauditquery.boot.defaults="${BASEDIR}/bin/boot.properties" -Dauditquery.home="${BASEDIR}" -Djava.net.preferIPv4Stack=true ${MAIN_CLASS} "$@"
ret=$?
if [ $ret == 0 -o $ret == 130 ]; then
  exit 0
else
  exit $ret
fi