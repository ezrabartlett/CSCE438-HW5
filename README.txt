***************************
Put your tweets file into the root directory of your mnt folder

File structure example:

	Mnt/
	|-Tweets.txt
	|-HW5
		|-Problem1.java
		|-problem2.java


*** Starting Docker ***

docker run -it --volume <current path>:/mnt/ sequenceiq/hadoop-docker:2.7.0 /etc/bootstrap.sh -bash

*** Running ***

Setup -- Run all of these as soon as you start docker
-----------------------------------------------------------------------
export JAVA_HOME=/usr/java/default
export PATH=${JAVA_HOME}/bin:${PATH}
export HADOOP_CLASSPATH=${JAVA_HOME}/lib/tools.jar
export HADOOP_CLASSPATH=/usr/java/default/lib/tools.jar
export PATH=$PATH:/usr/local/hadoop/bin


hdfs dfs -mkdir /user/root/data
hdfs dfs -put "mnt/tweets2009-06.txt" /user/root/data

Change to file directory
-----------------------------------------------------------------------
cd mnt/HW5

Compile <run in HW5 directory>
-----------------------------------------------------------------------
hadoop com.sun.tools.javac.Main Problem1.java
jar cf wc.jar Problem1*.class

Execute <run in HW5 directory>
-----------------------------------------------------------------------
hadoop jar P1.jar Problem1 /user/root/data /user/root/output

Delete -- I seem to have to do this everytime I want to run a new job
-----------------------------------------------------------------------
hdfs dfs -rm -f -r -skipTrash /user/root/output
hadoop job -kill job_1588207519446_0002

cat -- For printing out the file
-----------------------------------------------------------------------
hdfs dfs -cat /user/root/output/part-r-00000
