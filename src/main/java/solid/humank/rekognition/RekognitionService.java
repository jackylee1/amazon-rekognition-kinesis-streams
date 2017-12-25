package solid.humank.rekognition;

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

        AWSCredentials credentials=  AmazonClientUtil.generateCredentials();
        AmazonRekognition amazonRekognition =  AmazonClientUtil.getAmazonRekognition(Regions.US_WEST_2,credentials);

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

        return createStreamProcessorResult.getStreamProcessorArn();
    }

    public String compareWithIndexedFacesForIncomingBuddy(String s3bucketName, String fileName, String collectionId) {

        AWSCredentials credentials=  AmazonClientUtil.generateCredentials();
        AmazonRekognition amazonRekognition =  AmazonClientUtil.getAmazonRekognition(Regions.US_WEST_2,credentials);

        StringBuffer resultStringBuffer = new StringBuffer();

        ObjectMapper objectMapper = new ObjectMapper();

        // Get the incoming buddy image object from S3 bucket.
        Image image=new Image()
                .withS3Object(new S3Object()
                        .withBucket(s3bucketName)
                        .withName(fileName));

        // Search collection for faces similar to the largest face in the image.
        SearchFacesByImageRequest searchFacesByImageRequest = new SearchFacesByImageRequest()
                .withCollectionId(collectionId)
                .withImage(image)
                .withFaceMatchThreshold(70F)
                .withMaxFaces(2);

        SearchFacesByImageResult searchFacesByImageResult =
                amazonRekognition.searchFacesByImage(searchFacesByImageRequest);

        logger.info("Faces matching largest face in image from" + fileName);
        List< FaceMatch > faceImageMatches = searchFacesByImageResult.getFaceMatches();


        for (FaceMatch face: faceImageMatches) {
            try {
                resultStringBuffer.append(objectMapper.writerWithDefaultPrettyPrinter()
                        .writeValueAsString(face));
            } catch (JsonProcessingException e) {
                logger.error(e.getMessage());
            }
        }
        return resultStringBuffer.toString();
    }

    public String startStreamProcessor(String streamProcessorName) {
        AWSCredentials credentials=  AmazonClientUtil.generateCredentials();
        AmazonRekognition rekognitionClient =  AmazonClientUtil.getAmazonRekognition(Regions.US_WEST_2,credentials);
        StartStreamProcessorResult startStreamProcessorResult =
                rekognitionClient.startStreamProcessor(new StartStreamProcessorRequest().withName(streamProcessorName));
        logger.info("{}", startStreamProcessorResult.getSdkResponseMetadata().toString());
        return startStreamProcessorResult.getSdkResponseMetadata().toString();
    }
}
