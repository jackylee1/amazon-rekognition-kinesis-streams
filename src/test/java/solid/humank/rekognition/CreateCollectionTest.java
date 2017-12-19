package solid.humank.rekognition;

import com.amazonaws.regions.Regions;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CreateCollectionTest {

    @Test
    public void create_collection_on_us_west_2_region(){

        CreateCollection cc = new CreateCollection();
        String creationResult = cc.createCollectionAt(Regions.US_WEST_2);

        assertEquals("200",creationResult);

    }
}
