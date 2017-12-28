package solid.humank.app;

import com.amazonaws.services.rekognition.AmazonRekognition;
import com.amazonaws.services.rekognition.model.StopStreamProcessorRequest;
import com.amazonaws.services.rekognition.model.StopStreamProcessorResult;
import org.apache.logging.log4j.Logger;
import solid.humank.utils.AmazonClientUtil;
import solid.humank.utils.ResourceProperties;


public class StopStreamProcessor {

    private static final Logger logger = org.apache.logging.log4j.LogManager.getLogger();

    public static void main(String[] args){
        AmazonRekognition rekognitionClient =  AmazonClientUtil.getAmazonRekognition();
        StopStreamProcessorResult stopStreamProcessorResult =
                rekognitionClient.stopStreamProcessor(new StopStreamProcessorRequest().withName(ResourceProperties.getPropertyValue("streamProcessorName")));
        logger.info("{}", stopStreamProcessorResult.getSdkResponseMetadata().toString());
    }
}
