package solid.humank.rekognition;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import solid.humank.utils.ResourceProperties;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MiscTest {

    @Test
    public void getProperties(){

        assertEquals("us-west-2", ResourceProperties.getPropertyValue("region"));
    }

    @Test
    public void readValueFromJson() throws IOException {

        String jsonString = "{\n" +
                "  \"InputInformation\": {\n" +
                "    \"KinesisVideo\": {\n" +
                "      \"StreamArn\": \"arn:aws:kinesisvideo:us-west-2:584518143473:stream/myDemoVideoStream/1514474183623\",\n" +
                "      \"FragmentNumber\": \"91343852333184854440236179499425357012192381701\",\n" +
                "      \"ServerTimestamp\": 1.51447654268E9,\n" +
                "      \"ProducerTimestamp\": 1.514476540795E9,\n" +
                "      \"FrameOffsetInSeconds\": 0.0\n" +
                "    }\n" +
                "  },\n" +
                "  \"StreamProcessorInformation\": {\n" +
                "    \"Status\": \"RUNNING\"\n" +
                "  },\n" +
                "  \"FaceSearchResponse\": [\n" +
                "    {\n" +
                "      \"DetectedFace\": {\n" +
                "        \"BoundingBox\": {\n" +
                "          \"Height\": 0.7638889,\n" +
                "          \"Width\": 0.4296875,\n" +
                "          \"Left\": 0.4265625,\n" +
                "          \"Top\": -0.06666667\n" +
                "        },\n" +
                "        \"Confidence\": 99.958084,\n" +
                "        \"Landmarks\": [\n" +
                "          {\n" +
                "            \"X\": 0.59380096,\n" +
                "            \"Y\": 0.19286713,\n" +
                "            \"Type\": \"eyeLeft\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"X\": 0.71759623,\n" +
                "            \"Y\": 0.20158546,\n" +
                "            \"Type\": \"eyeRight\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"X\": 0.6902272,\n" +
                "            \"Y\": 0.28199735,\n" +
                "            \"Type\": \"nose\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"X\": 0.6243566,\n" +
                "            \"Y\": 0.43837258,\n" +
                "            \"Type\": \"mouthLeft\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"X\": 0.70930564,\n" +
                "            \"Y\": 0.42977205,\n" +
                "            \"Type\": \"mouthRight\"\n" +
                "          }\n" +
                "        ],\n" +
                "        \"Pose\": {\n" +
                "          \"Pitch\": -2.9396436,\n" +
                "          \"Roll\": 0.99219453,\n" +
                "          \"Yaw\": 14.551514\n" +
                "        },\n" +
                "        \"Quality\": {\n" +
                "          \"Brightness\": 43.346573,\n" +
                "          \"Sharpness\": 99.99453\n" +
                "        }\n" +
                "      },\n" +
                "      \"MatchedFaces\": 1\n" +
                "    }\n" +
                "  ]\n" +
                "}\n";
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(jsonString);
        JsonNode first = root.path("FaceSearchResponse").findPath("MatchedFaces");



        assertEquals(1,first.intValue());

        ;

    }
}
