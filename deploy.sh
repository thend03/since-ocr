#!/bin/bash

git pull origin main
mvn clean package
ps -ef|grep since-ocr|grep -v grep|awk '{print $2}'|xargs -r kill
nohup java -jar target/since-ocr-0.0.1-SNAPSHOT.jar >/dev/null &


