#!/usr/bin/env bash

aws lambda update-function-code \
--function-name FaceIndexHandler \
--zip-file fileb://~/git/amazon-rekognition-kinesis-streams/target/amazon-rekognition-kinesis-streams-1.0-SNAPSHOT.jar

aws lambda update-function-code \
--function-name RekognitionHandler \
--zip-file fileb://~/git/amazon-rekognition-kinesis-streams/target/amazon-rekognition-kinesis-streams-1.0-SNAPSHOT.jar