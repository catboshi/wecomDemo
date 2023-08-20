FROM java:8
MAINTAINER wecom

VOLUME /tmp

COPY wecom-0.0.1.RELEASE.jar wecom-1.0.0.jar

ENV TZ=Asia/Shanghai
RUN ln -sf /usr/share/zoneinfo/{TZ} /etc/localtime && echo "{TZ}" > /etc/timezone

EXPOSE 9081
EXPOSE 9000
EXPOSE 7098
EXPOSE 7099

ENTRYPOINT ["java", \
"-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=9000", \
"-agentpath:/usr/local/src/jrebel/lib/libjrebel64.so", \
"-Drebel.remoting_plugin=true", \
"-Djava.rmi.server.hostname=43.142.158.6", \
"-Dcom.sun.management.jmxremote", \
"-Dcom.sun.management.jmxremote.rmi.port=7098", \
"-Dcom.sun.management.jmxremote.port=7099", \
"-Dcom.sun.management.jmxremote.authenticate=false", \
"-Dcom.sun.management.jmxremote.ssl=false", \
"-XX:+HeapDumpOnOutOfMemoryError", \
"-XX:HeapDumpPath=/tmp/gc.hprof", \
"-jar", \
"wecom-1.0.0.jar"]