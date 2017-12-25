package solid.humank.rekognition;

import com.amazonaws.regions.Regions;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import solid.humank.utils.DateTimeUtil;

import static org.junit.Assert.assertEquals;

public class CollectionServiceTest {

    String currentDateTime;
    String collectionId = "myCollection";

    private static final Logger logger = LogManager.getLogger();

    @BeforeEach
    public void init(){
        currentDateTime = DateTimeUtil.getCurrentDateTimeInYMDHMS();
        collectionId = collectionId.concat(currentDateTime);
    }

    @Test
    public void create_collection_on_us_west_2_region(){

        CollectionService cc = new CollectionService();
        String creationResult = cc.createCollectionAt(Regions.US_WEST_2,collectionId);
        logger.info("The created collection id is : {}", collectionId);
        assertEquals("200",creationResult);

    }

    @AfterEach
    public void delete_collection_on_us_west_2_region(){
        CollectionService cc = new CollectionService();
        String deletionResult = cc.deleteCollection(Regions.US_WEST_2,collectionId);
        assertEquals("200",deletionResult);
    }
}
