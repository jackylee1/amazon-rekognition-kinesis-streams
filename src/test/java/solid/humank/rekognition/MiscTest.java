package solid.humank.rekognition;

import org.junit.jupiter.api.Test;
import solid.humank.utils.ResourceProperties;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MiscTest {

    @Test
    public void getProperties(){

        assertEquals("us-west-2", ResourceProperties.getPropertyValue("region"));
    }

}
