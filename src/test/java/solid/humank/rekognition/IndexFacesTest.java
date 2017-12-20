package solid.humank.rekognition;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.rekognition.model.FaceRecord;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import solid.humank.utils.DateTimeUtil;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class IndexFacesTest {

    private String s3BucketName="rekognition-lab";
    private String fileName ="001.png";
    String currentDateTime;
    String collectionId = "myCollection";

    @Before
    public void init(){
        currentDateTime = DateTimeUtil.getCurrentDateTimeInYMDHMS();
        collectionId = collectionId.concat(currentDateTime);
    }

    @Test
    public void add_face_by_single_image(){

        createCollection();

        IndexFaces indexFaces = new IndexFaces();
        List<FaceRecord> faceRecords = indexFaces.indexFaceFor(Regions.US_WEST_2, collectionId, s3BucketName, fileName);

        assertEquals(1, faceRecords.size());
    }

    private void createCollection(){
        CollectionService cc = new CollectionService();
        String creationResult = cc.createCollectionAt(Regions.US_WEST_2,collectionId);

    }


}
