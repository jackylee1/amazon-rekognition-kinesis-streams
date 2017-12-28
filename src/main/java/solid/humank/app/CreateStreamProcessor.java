package solid.humank.app;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import solid.humank.service.RekognitionService;
import solid.humank.utils.ResourceProperties;

public class CreateStreamProcessor {

    private static float matchThreshold = 85.5f;
    private static final Logger logger = LogManager.getLogger();
    public static void main(String[] args){

        String streamProcessorName = ResourceProperties.getPropertyValue("streamProcessorName");
        String kinesisVideoStreamArn = ResourceProperties.getPropertyValue("videoStreamArn");
        String dataStreamArn = ResourceProperties.getPropertyValue("dataStreamArn");
        String collectionId = ResourceProperties.getPropertyValue("collectionId");
        String roleArn = ResourceProperties.getPropertyValue("roleArn");

        RekognitionService rekognitionService = new RekognitionService();
        String createdProcessorArn =
                rekognitionService.createVideoStreamProcessor(kinesisVideoStreamArn, dataStreamArn, collectionId, matchThreshold, roleArn, streamProcessorName);
        logger.info(createdProcessorArn);
    }
}
