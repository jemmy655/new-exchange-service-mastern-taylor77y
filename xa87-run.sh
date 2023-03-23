#!/bin/bash
kill -s 9 `ps -aux | grep 'www/java/xa87-' | awk '{print $2}'`
nohup java  -server -Xms200m -Xmx300m  -jar /www/java/xa87-member-service-1.0.0.jar  &
nohup java  -server -Xms200m -Xmx300m  -jar /www/java/xa87-job-service-1.0.0.jar  &
nohup java  -server -Xms200m -Xmx300m  -jar /www/java/xa87-data-service-1.0.0.jar  &
nohup java  -server -Xms200m -Xmx300m  -jar /www/java/xa87-contract-service-1.0.0.jar  &
nohup java  -server -Xms200m -Xmx300m  -jar /www/java/xa87-entrust-consumer-1.0.0.jar  &
nohup java  -server -Xms200m -Xmx300m  -jar /www/java/xa87-entrust-service-1.0.0.jar  &
nohup java  -server -Xms200m -Xmx300m  -jar /www/java/xa87-otc-service-1.0.0.jar  &
nohup java  -server -Xms200m -Xmx300m  -jar /www/java/xa87-rabbitmq-consumer-1.0.0.jar  &
nohup java  -server -Xms200m -Xmx300m  -jar /www/java/xa87-socket-service-1.0.0.jar  &
nohup java  -server -Xms200m -Xmx300m  -jar /www/java/xa87-wallet-service-1.0.0.jar  &
nohup java  -server -Xms200m -Xmx300m  -jar /www/java/xa87-gateway-zuul-1.0.0.jar &
