package solid.humank.service;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.rekognition.AmazonRekognition;
import com.amazonaws.services.rekognition.model.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import solid.humank.utils.AmazonClientUtil;

import java.util.List;

public class RekognitionService {

    private static final Logger logger = LogManager.getLogger();

    public String createVideoStreamProcessor(String kinesisVideoStreamArn, String kinesisDataStreamArn, String collectionId, float matchThreshold, String roleArn, String streamProcessorName ){

        AmazonRekognition amazonRekognition =  AmazonClientUtil.getAmazonRekognition();

        KinesisVideoStream kinesisVideoStream = new KinesisVideoStream().withArn(kinesisVideoStreamArn);
        StreamProcessorInput streamProcessorInput =
                new StreamProcessorInput().withKinesisVideoStream(kinesisVideoStream);
        KinesisDataStream kinesisDataStream = new KinesisDataStream().withArn(kinesisDataStreamArn);
        StreamProcessorOutput streamProcessorOutput =
                new StreamProcessorOutput().withKinesisDataStream(kinesisDataStream);
        FaceSearchSettings faceSearchSettings =
                new FaceSearchSettings().withCollectionId(collectionId).withFaceMatchThreshold(matchThreshold);
        StreamProcessorSettings streamProcessorSettings =
                new StreamProcessorSettings().withFaceSearch(faceSearchSettings);

        CreateStreamProcessorResult createStreamProcessorResult = amazonRekognition.createStreamProcessor(
                new CreateStreamProcessorRequest().withInput(streamProcessorInput).withOutput(streamProcessorOutput)
                        .withSettings(streamProcessorSettings).withRoleArn(roleArn).withName(streamProcessorName));

        logger.info("createStreamProcessorResult : {}",createStreamProcessorResult);
        return createStreamProcessorResult.getStreamProcessorArn();
    }

    public String compareWithIndexedFacesForIncomingBuddy(String s3bucketName, String fileName, String collectionId) {

        AmazonRekognition amazonRekognition =  AmazonClientUtil.getAmazonRekognition();

        StringBuffer resultStringBuffer = new StringBuffer();

        ObjectMapper objectMapper = new ObjectMapper();

        // Get the incoming buddy image object from S3 bucket.
        Image image=new Image()
                .withS3Object(new S3Object()
                        .withBucket(s3bucketName)
                        .withName(fileName));

        logger.info(image.toString());

        // Search collection for faces similar to the largest face in the image.
        SearchFacesByImageRequest searchFacesByImageRequest = new SearchFacesByImageRequest()
                .withCollectionId(collectionId)
                .withImage(image)
                .withFaceMatchThreshold(70F)
                .withMaxFaces(100);

        logger.info(searchFacesByImageRequest.toString());

        SearchFacesByImageResult searchFacesByImageResult =
                amazonRekognition.searchFacesByImage(searchFacesByImageRequest);

        logger.info("searchFacesByImageResult : {}",searchFacesByImageResult);

        logger.info("Faces matching largest face in image from" + fileName);
        List< FaceMatch > faceImageMatches = searchFacesByImageResult.getFaceMatches();

        logger.info("MatchResult : {}", searchFacesByImageResult);
        logger.info("check face is matched or not : {}", faceImageMatches.size());

        if(faceImageMatches.size()>0){
            for (FaceMatch face: faceImageMatches) {
                try {
                    resultStringBuffer.append(objectMapper.writerWithDefaultPrettyPrinter()
                            .writeValueAsString(face));
                } catch (JsonProcessingException e) {
                    logger.error(e.getMessage());
                }
            }
        }else{
            resultStringBuffer.append("{ \"Result\":\"Not Matched\"}");
        }
        return resultStringBuffer.toString();
    }

    public String startStreamProcessor(String streamProcessorName) {
        AmazonRekognition rekognitionClient =  AmazonClientUtil.getAmazonRekognition();
        StartStreamProcessorResult startStreamProcessorResult =
                rekognitionClient.startStreamProcessor(new StartStreamProcessorRequest().withName(streamProcessorName));
        logger.info("{}", startStreamProcessorResult.getSdkResponseMetadata().toString());
        return startStreamProcessorResult.getSdkResponseMetadata().toString();
    }
}
