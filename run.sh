#!/bin/bash
cd $(dirname $0)
exec dbus-launch java -cp target/classes:/home/siasia/.m2/repository/aopalliance/aopalliance/1.0/aopalliance-1.0.jar:/home/siasia/.m2/repository/com/google/code/findbugs/jsr305/1.3.9/jsr305-1.3.9.jar:/home/siasia/.m2/repository/com/google/guava/guava/12.0/guava-12.0.jar:/home/siasia/.m2/repository/com/google/inject/guice/3.0/guice-3.0.jar:/home/siasia/.m2/repository/javax/inject/javax.inject/1/javax.inject-1.jar:/home/siasia/.m2/repository/javax/servlet/servlet-api/2.5/servlet-api-2.5.jar:/usr/share/java/dbus-java/dbus.jar:/home/siasia/.m2/repository/org/mortbay/jetty/jetty/6.1.26/jetty-6.1.26.jar:/home/siasia/.m2/repository/org/mortbay/jetty/jetty-util/6.1.26/jetty-util-6.1.26.jar:/home/siasia/.m2/repository/org/mortbay/jetty/servlet-api/2.5-20081211/servlet-api-2.5-20081211.jar com.skype.headless.MainModule :1 $(readlink -f target/home) username password