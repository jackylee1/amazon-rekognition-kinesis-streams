#!/usr/bin/env bash

aws lambda create-function \
--function-name FaceIndexHandler \
--runtime java8 \
--role arn:aws:iam::584518143473:role/LambdaRole \
--handler solid.humank.handler.FaceIndexHandler::handleRequest \
--tags Name=booth-demo \
--zip-file fileb://~/git/amazon-rekognition-kinesis-streams/target/amazon-rekognition-kinesis-streams-1.0-SNAPSHOT.jar \
--memory-size 1536 \
--timeout 300

aws lambda create-function \
--function-name RekognitionHandler \
--runtime java8 \
--role arn:aws:iam::584518143473:role/LambdaRole \
--handler solid.humank.handler.RekognitionHandler::handleRequest \
--tags Name=booth-demo \
--zip-file fileb://~/git/amazon-rekognition-kinesis-streams/target/amazon-rekognition-kinesis-streams-1.0-SNAPSHOT.jar \
--memory-size 1536 \
--timeout 300


