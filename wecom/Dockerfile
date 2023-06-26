FROM java:8
MAINTAINER wecom

VOLUME /tmp

COPY wecom-0.0.1.RELEASE.jar wecom-1.0.0.jar

ENV TZ=Asia/Shanghai
RUN ln -sf /usr/share/zoneinfo/{TZ} /etc/localtime && echo "{TZ}" > /etc/timezone

EXPOSE 9081

ENTRYPOINT ["java","-jar","wecom-1.0.0.jar"]