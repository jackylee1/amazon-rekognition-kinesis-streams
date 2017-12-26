package solid.humank.app;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import solid.humank.service.RekognitionService;

public class CreateStreamProcessorApp {

    private static String streamProcessorName ="myVideoStreamProcessor";
    private static String kinesisVideoStreamArn = "arn:aws:kinesisvideo:us-west-2:584518143473:stream/MyKinesisVideoStream/1513753760664";
    private static String kinesisDataStreamArn = "arn:aws:kinesis:us-west-2:584518143473:stream/MyKinesisDataStream";
    private static String roleArn = "arn:aws:iam::584518143473:role/rekognition-role";

    private static float matchThreshold = 85.5f;

    private static final Logger logger = LogManager.getLogger();
    static String currentDateTime;
    static String collectionId = "myCollection";

    public static void main(String[] args){

        RekognitionService rekognitionService = new RekognitionService();
        String createdProcessorArn =
                rekognitionService.createVideoStreamProcessor(kinesisVideoStreamArn, kinesisDataStreamArn, collectionId, matchThreshold, roleArn, streamProcessorName);
        logger.info(createdProcessorArn);
    }
}
