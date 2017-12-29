package solid.humank.rekognition;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.StringUtils;
import solid.humank.service.RekognitionService;

import static org.junit.jupiter.api.Assertions.*;

public class RekognitionSearchTest {

    String s3BucketName="rekognition-lab";

    String collectionId="myCollection";

    private static Logger logger = LogManager.getLogger();

    @Test
    public void compare_with_indexed_faces_ivan_not_matched(){
        String fileName ="test-ivan-01.jpg";
        RekognitionService rekognitionService = new RekognitionService();
        String result = rekognitionService.compareWithIndexedFacesForIncomingBuddy(s3BucketName,fileName,collectionId);

        logger.info(result);
        assertEquals("{ \"Result\":\"Not Matched\"}",result);
    }

    @Test
    public void compare_with_indexed_faces_then_matched() throws JsonProcessingException {
        String fileName ="kim-001.png";
        RekognitionService rekognitionService = new RekognitionService();
        String result = rekognitionService.compareWithIndexedFacesForIncomingBuddy(s3BucketName,fileName,collectionId);
        assertEquals("{ \"Result\":\"Not Matched\"}",result);
    }
}
