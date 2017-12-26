package solid.humank.rekognition;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.rekognition.model.FaceRecord;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import solid.humank.service.CollectionService;
import solid.humank.service.IndexFaceService;
import solid.humank.utils.DateTimeUtil;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class IndexFacesServiceTest {

    private static final Logger logger = LogManager.getLogger();

    private String s3BucketName="rekognition-index";
    private String fileName ="001.png";

    String currentDateTime;
    String collectionId = "myCollection";


    @BeforeEach
    public void initCollectionNaming(){
        currentDateTime = DateTimeUtil.getCurrentDateTimeInYMDHMS();
        collectionId = collectionId.concat(currentDateTime);
    }

    @Test
    public void add_face_by_single_image(){

        CollectionService cc = new CollectionService();
        String creationResult = cc.createCollectionAt(Regions.US_WEST_2,collectionId);

        IndexFaceService indexFaceService = new IndexFaceService();
        List<FaceRecord> faceRecords = indexFaceService.indexFaceFor(collectionId, s3BucketName, fileName);

        assertEquals(1, faceRecords.size());
    }

    @AfterEach
    public void deleteCollection(){
        CollectionService cc = new CollectionService();
        String deletionResult = cc.deleteCollection(Regions.US_WEST_2,collectionId);
        assertEquals("200",deletionResult);
    }

}
