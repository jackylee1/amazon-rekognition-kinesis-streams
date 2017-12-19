package solid.humank.rekognition;

import com.amazonaws.regions.Regions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import solid.humank.utils.DateTimeUtil;

import java.util.Date;

import static org.junit.Assert.assertEquals;

public class CollectionTest {

    String currentDateTime;
    String collectionId = "myCollection";

    @Before
    public void init(){
        currentDateTime = DateTimeUtil.getCurrentDateTimeInYMDHMS();
        collectionId = collectionId.concat(currentDateTime);
    }

    @Test
    public void create_collection_on_us_west_2_region(){

        CollectionController cc = new CollectionController();
        String creationResult = cc.createCollectionAt(Regions.US_WEST_2,collectionId);

        assertEquals("200",creationResult);

    }

    @After
    public void delete_collection_on_us_west_2_region(){
        CollectionController cc = new CollectionController();
        String deletionResult = cc.deleteCollection(Regions.US_WEST_2,collectionId);
        assertEquals("200",deletionResult);
    }
}
