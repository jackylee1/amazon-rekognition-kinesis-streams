package solid.humank.rekognition;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.StringUtils;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class RekognitionSearchTest {

    String s3BucketName="rekognition-lab";
    String fileName ="incomingbuddy.png";
    String collectionId="demoCollection";

    private static Logger logger = LogManager.getLogger();

    @BeforeEach
    public void init(){
        logger.info("init starting...");
    }

    @Test
    public void compare_with_indexed_faces_for_incoming_buddy() throws JsonProcessingException {

        RekognitionService rekognitionService = new RekognitionService();
        String result = rekognitionService.compareWithIndexedFacesForIncomingBuddy(s3BucketName,fileName,collectionId);

        logger.info(result);
        assertTrue(StringUtils.isNotBlank(result));
    }
}
