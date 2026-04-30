#!/bin/bash -e
#
# Copyright (c) 2024 Oracle and/or its affiliates. All rights reserved.
#
# This program and the accompanying materials are made available under the
# terms of the Eclipse Distribution License v. 1.0, which is available at
# http://www.eclipse.org/org/documents/edl-v10.php.
#
# SPDX-License-Identifier: BSD-3-Clause
#

[[ -z ${1} ]] && SUMMARY_FILE_NAME='SUMMARY.TXT' || SUMMARY_FILE_NAME=${1}

EE4J_PARENT_URL='https://repo.eclipse.org/content/repositories/maven_central/org/eclipse/ee4j/project/1.0.9/project-1.0.9.pom'
#SAAJ_TCK_BUNDLE='https://ci.eclipse.org/metro/job/saaj-tck-build/lastSuccessfulBuild/artifact/standalone-bundles/soap-tck-2.0.0.zip'

# Download and extract TCK tests
wget -q ${SAAJ_TCK_BUNDLE} -O soap-tck.zip && unzip -qq soap-tck.zip
mkdir soap-tck/JTwork
mkdir soap-tck/JTreport

# Download SAAJ API dependencies from Maven Central
mkdir 'saaj'
mkdir 'ri-download'
echo '<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
        xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
        xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
  <modelVersion>4.0.0</modelVersion>
  <parent>
    <groupId>org.eclipse.ee4j</groupId>
    <artifactId>project</artifactId>
    <version>1.0.9</version>
  </parent>
  <groupId>download</groupId>
  <artifactId>ri</artifactId>
  <version>1.0.0</version>
  <dependencies>
    <dependency>
      <groupId>com.sun.xml.messaging.saaj</groupId>
      <artifactId>saaj-impl</artifactId>
      <version>'${SAAJ_RI_VERSION}'</version>
    </dependency>
    <dependency>
      <groupId>jakarta.xml.soap</groupId>
      <artifactId>jakarta.xml.soap-api</artifactId>
      <version>'${SAAJ_API_VERSION}'</version>
    </dependency>
  </dependencies>
</project>
' > 'ri-download/pom.xml'

mvn -f ri-download/pom.xml \
    -Psnapshots \
    -DoutputDirectory="${WORKSPACE}/saaj" \
    org.apache.maven.plugins:maven-dependency-plugin:3.1.2:copy-dependencies

# Download and extract Tomcat
wget -nv ${TOMCAT_URL} --no-check-certificate -O - | tar xfz -
mv apache-tomcat-* apache-tomcat

# Define local variables
CATALINA_HOME="${WORKSPACE}/apache-tomcat"
CATALINA_DEPLOY_DIR="${CATALINA_HOME}/webapps"
TS_HOME="${WORKSPACE}/soap-tck"
TS_WORK_DIR="${TS_HOME}/JTwork"
TS_REPORT_DIR="${TS_HOME}/JTreport"
SAAJ_HOME="${WORKSPACE}/saaj"

# Build local.classes classpath
SAAJ_LIBS="${SAAJ_HOME}/*.jar"
LOCAL_CLASSES="${CATALINA_HOME}lib/servlet-api.jar"
for lib in ${SAAJ_LIBS}; do
  LOCAL_CLASSES="$lib:${LOCAL_CLASSES}"
done

# Configure TCK tests
echo step1
mv soap-tck/bin/ts.jte soap-tck/bin/ts.jte.orig
#mv soap-tck/bin/ts.jte.jdk11 soap-tck/bin/ts.jte.jdk11.orig
#mv soap-tck/bin/ts.jte.jdk11 soap-tck/bin/ts.jte
echo step2
sed -e "s#webcontainer.home=.*#webcontainer.home=${CATALINA_HOME}#" \
    -e "s#impl.vi.deploy.dir=.*#impl.vi.deploy.dir=${CATALINA_DEPLOY_DIR}#" \
    -e 's#impl.vi=.*#impl.vi=tomcat#' \
    -e "s#local.classes=.*#local.classes=${LOCAL_CLASSES}#" \
    -e "s#work.dir=.*#work.dir=${TS_WORK_DIR}#" \
    -e "s#report.dir=.*#report.dir=${TS_REPORT_DIR}#" \
    soap-tck/bin/ts.jte.orig > soap-tck/bin/ts.jte
# Print TCK tests configuration
echo '- [ ts.jte ]--------------------------------------------------------------------'
cat soap-tck/bin/ts.jte
echo '--------------------------------------------------------------------------------'

## Copy TCK web applications to Tomcat for deployment
#cp -v ${TS_HOME}/dist/*.war ${CATALINA_DEPLOY_DIR}

# Copy TCK dependencies to Tomcat
mkdir "${CATALINA_HOME}/shared"
mkdir "${CATALINA_HOME}/shared/lib"
cp -v ${TS_HOME}/lib/tsharness.jar \
      ${TS_HOME}/lib/saajtck.jar \
      ${SAAJ_HOME}/*.jar \
   ${CATALINA_HOME}/shared/lib

mv -v ${CATALINA_HOME}/conf/catalina.properties ${CATALINA_HOME}/conf/catalina.properties.orig
sed -e 's/shared.loader=.*/shared.loader="\$\{catalina.base\}\/shared\/lib\/\*\.jar"/' \
    ${CATALINA_HOME}/conf/catalina.properties.orig > ${CATALINA_HOME}/conf/catalina.properties

echo '- [ catalina.properties ]-------------------------------------------------------'
cat ${CATALINA_HOME}/conf/catalina.properties
echo '--------------------------------------------------------------------------------'

# Start Tomcat
${CATALINA_HOME}/bin/startup.sh

echo -n 'Waiting for log file '
count='0'
while [ "$count" -lt '60' -a ! -f ${CATALINA_HOME}/logs/catalina.*.log ]; do
  echo -n '.'
  sleep 1
  count=$(( count + 1 ))
done
echo ' done'

# Wait for Tomcat to deploy all aplication and start up
CATALINA_LOG="${CATALINA_HOME}/logs/catalina.*.log"
CATALINA_LOG_FIFO='/tmp/catalina.fifo'
mkfifo ${CATALINA_LOG_FIFO}
tail -f ${CATALINA_LOG} > ${CATALINA_LOG_FIFO} &
CATALINA_LOG_TAIL_PID=$!
grep -m 1 'org.apache.catalina.startup.Catalina.start Server startup in' ${CATALINA_LOG_FIFO}
kill -TERM ${CATALINA_LOG_TAIL_PID} || true

# Print Tomcat log file
echo '- [ Tomcat log ]----------------------------------------------------------------'
cat ${CATALINA_LOG}
echo '--------------------------------------------------------------------------------'

# Run TCK tests
cd ${TS_HOME}/bin

ant config.vi deploy.all

ant run.all | tee run.log

export NAME=${SOAP_TCK_BUNDLE##*/}

#wget -q ${SAAJ_TCK_BUNDLE} -O ${NAME}

echo '***********************************************************************************' >> ${WORKSPACE}/${SUMMARY_FILE_NAME}
echo '***                        TCK bundle information                               ***' >> ${WORKSPACE}/${SUMMARY_FILE_NAME}
echo "*** Name:       ${NAME}                                     ***" >> ${WORKSPACE}/${SUMMARY_FILE_NAME}
echo "*** Download URL:  ${SAAJ_TCK_BUNDLE} ***"  >> ${WORKSPACE}/${SUMMARY_FILE_NAME}
echo '*** Date and size: '`stat -c "date: %y, size(b): %s" ${WORKSPACE}/soap-tck.zip`'        ***'>> ${WORKSPACE}/${SUMMARY_FILE_NAME}
echo "*** SHA256SUM: "`sha256sum ${WORKSPACE}/soap-tck.zip | awk '{print $1}'`' ***' >> ${WORKSPACE}/${SUMMARY_FILE_NAME}
echo '***                                                                             ***' >> ${WORKSPACE}/${SUMMARY_FILE_NAME}
echo '***                        MVN/JDK info                                         ***' >> ${WORKSPACE}/${SUMMARY_FILE_NAME}
mvn -v | tee -a ${WORKSPACE}/${SUMMARY_FILE_NAME} || true
echo '***********************************************************************************' >> ${WORKSPACE}/${SUMMARY_FILE_NAME}
echo '***                        TCK results summary                                  ***' >> ${WORKSPACE}/${SUMMARY_FILE_NAME}
cat run.log | sed -e '1,/Completed running/d' >> ${WORKSPACE}/${SUMMARY_FILE_NAME}
cat ${WORKSPACE}/${SUMMARY_FILE_NAME}
#cat SUMMARY.TXT | grep "FAILED" > FAILURES.TXT
#cat SUMMARY.TXT | grep "ERROR" > ERRORS.TXT
PASSED_COUNT=`head -10 ${WORKSPACE}/${SUMMARY_FILE_NAME} | tail -1 | sed 's/.*=\s\(.*\)/\1/'`
FAILED_COUNT=`head -11 ${WORKSPACE}/${SUMMARY_FILE_NAME} | tail -1 | sed 's/.*=\s\(.*\)/\1/'`
ERROR_COUNT=`head -12  ${WORKSPACE}/${SUMMARY_FILE_NAME} | tail -1 | sed 's/.*=\s\(.*\)/\1/'`

  echo "ERROR_COUNT:  ${ERROR_COUNT}"
  echo "FAILED_COUNT: ${FAILED_COUNT}"
  echo "PASSED_COUNT: ${PASSED_COUNT}"

${CATALINA_HOME}/bin/shutdown.sh
