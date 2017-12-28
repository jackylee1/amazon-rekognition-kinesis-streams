package solid.humank.app;

import com.amazonaws.services.rekognition.AmazonRekognition;
import com.amazonaws.services.rekognition.model.DeleteStreamProcessorRequest;
import com.amazonaws.services.rekognition.model.DeleteStreamProcessorResult;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import solid.humank.utils.AmazonClientUtil;
import solid.humank.utils.ResourceProperties;

public class DeleteStreamProcessr {

    private static final Logger logger = LogManager.getLogger();

    public static void main(String[] args){
        AmazonRekognition rekognitionClient =  AmazonClientUtil.getAmazonRekognition();
        DeleteStreamProcessorResult deleteStreamProcessorResult = rekognitionClient
                .deleteStreamProcessor(new DeleteStreamProcessorRequest().withName(ResourceProperties.getPropertyValue("streamProcessorName")));

        logger.info("{}", deleteStreamProcessorResult.getSdkResponseMetadata().toString());
    }
}
