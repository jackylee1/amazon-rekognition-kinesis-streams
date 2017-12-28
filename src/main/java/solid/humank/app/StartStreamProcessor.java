package solid.humank.app;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import solid.humank.service.RekognitionService;
import solid.humank.utils.ResourceProperties;

public class StartStreamProcessor {

    private static final Logger logger = LogManager.getLogger();


    public static void main(String[] args){

        RekognitionService rekognitionService = new RekognitionService();
        String result = rekognitionService.startStreamProcessor(ResourceProperties.getPropertyValue("streamProcessorName"));
        logger.info(result);
    }
}
