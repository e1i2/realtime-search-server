#!/bin/bash
export TZ=Asia/Seoul
java -jar -Dreactor.netty.http.server.accessLogEnabled=true /app.jar