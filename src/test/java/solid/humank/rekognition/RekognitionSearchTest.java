package solid.humank.rekognition;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.StringUtils;
import solid.humank.service.RekognitionService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class RekognitionSearchTest {

    String s3BucketName="rekognition-lab";

    String collectionId="myCollection";

    private static Logger logger = LogManager.getLogger();

    @Test
    public void compare_with_indexed_faces_for_incoming_buddy() throws JsonProcessingException {
        String fileName ="incomingbuddy.jpg";
        RekognitionService rekognitionService = new RekognitionService();
        String result = rekognitionService.compareWithIndexedFacesForIncomingBuddy(s3BucketName,fileName,collectionId);

        logger.info(result);
        assertTrue(StringUtils.isNotBlank(result));
    }

    @Test
    public void compare_with_indexed_faces_then_matched(){
        String fileName ="test-ivan-01.jpg";
        RekognitionService rekognitionService = new RekognitionService();
        String result = rekognitionService.compareWithIndexedFacesForIncomingBuddy(s3BucketName,fileName,collectionId);

        logger.info(result);
        assertNotEquals("",result);
    }

    @Test
    public void compare_with_indexed_faces_then_not_matched() throws JsonProcessingException {
        String fileName ="test-01.jpg";
        RekognitionService rekognitionService = new RekognitionService();
        String result = rekognitionService.compareWithIndexedFacesForIncomingBuddy(s3BucketName,fileName,collectionId);
        assertEquals("",result);
    }
}
