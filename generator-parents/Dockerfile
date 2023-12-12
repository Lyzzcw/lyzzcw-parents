FROM openjdk:8-jdk

MAINTAINER lyzzcw.work

ADD generator-parents-1.0-SNAPSHOT.jar /home/app.jar

# JVM调优参数等额外参数
ENV PARAMS ""

ENV TZ=Asia/Shanghai

# 同步时区 
RUN ln -snf /usr/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

# 设置命令执行路径
WORKDIR /home

# 容器开放端口
EXPOSE 8001

ENTRYPOINT ["java", "-jar", "app.jar","$PARAMS"]
