package solid.humank.rekognition;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;
import solid.humank.model.FaceIndex;
import solid.humank.model.Recognition;
import solid.humank.persistence.RecognitionPersistence;
import solid.humank.utils.AmazonClientUtil;
import solid.humank.utils.ResourceProperties;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class MiscTest {

    private static final Logger logger = LogManager.getLogger();

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
                "      \"FragmentNumber\": \"91343852333363350464344257553389350256110796287\",\n" +
                "      \"ServerTimestamp\": 1.514512509666E9,\n" +
                "      \"ProducerTimestamp\": 1.514512508431E9,\n" +
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
                "          \"Height\": 0.31944445,\n" +
                "          \"Width\": 0.18046875,\n" +
                "          \"Left\": 0.4484375,\n" +
                "          \"Top\": 0.25555557\n" +
                "        },\n" +
                "        \"Confidence\": 99.99199,\n" +
                "        \"Landmarks\": [\n" +
                "          {\n" +
                "            \"X\": 0.5134555,\n" +
                "            \"Y\": 0.37227422,\n" +
                "            \"Type\": \"eyeLeft\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"X\": 0.5730344,\n" +
                "            \"Y\": 0.38269398,\n" +
                "            \"Type\": \"eyeRight\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"X\": 0.55972713,\n" +
                "            \"Y\": 0.4366241,\n" +
                "            \"Type\": \"nose\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"X\": 0.5147065,\n" +
                "            \"Y\": 0.49111134,\n" +
                "            \"Type\": \"mouthLeft\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"X\": 0.5706894,\n" +
                "            \"Y\": 0.49916843,\n" +
                "            \"Type\": \"mouthRight\"\n" +
                "          }\n" +
                "        ],\n" +
                "        \"Pose\": {\n" +
                "          \"Pitch\": 1.1949792,\n" +
                "          \"Roll\": 3.81565,\n" +
                "          \"Yaw\": 23.07207\n" +
                "        },\n" +
                "        \"Quality\": {\n" +
                "          \"Brightness\": 31.913412,\n" +
                "          \"Sharpness\": 99.98488\n" +
                "        }\n" +
                "      },\n" +
                "      \"MatchedFaces\": [\n" +
                "        {\n" +
                "          \"Similarity\": 93.875114,\n" +
                "          \"Face\": {\n" +
                "            \"BoundingBox\": {\n" +
                "              \"Height\": 0.472936,\n" +
                "              \"Width\": 0.630866,\n" +
                "              \"Left\": 0.154332,\n" +
                "              \"Top\": 0.126522\n" +
                "            },\n" +
                "            \"FaceId\": \"2ec07104-1469-4002-aca6-e2b571b38da7\",\n" +
                "            \"Confidence\": 99.9945,\n" +
                "            \"ImageId\": \"dd7e522d-8130-57d5-94f3-431f904b07fb\",\n" +
                "            \"ExternalImageId\": \"candy-001.jpg\"\n" +
                "          }\n" +
                "        }\n" +
                "      ]\n" +
                "    }\n" +
                "  ]\n" +
                "}";
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(jsonString);
        JsonNode matched = root.path("FaceSearchResponse").findPath("MatchedFaces");
        JsonNode detail = matched.get(0);
        String faceId = detail.path("Face").findPath("FaceId").textValue();

        assertEquals("2ec07104-1469-4002-aca6-e2b571b38da7",faceId);

    }

    @Test
    public void queryTest(){
        AmazonDynamoDB client = AmazonClientUtil.getAmazonDynamoDBClient();
        DynamoDBMapper mapper = new DynamoDBMapper(client);

        Map<String, AttributeValue> eav = new HashMap<String, AttributeValue>();
        eav.put(":val1", new AttributeValue().withS("2ec07104-1469-4002-aca6-e2b571b38da7"));
        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression()
                .withFilterExpression("faceId = :val1").withExpressionAttributeValues(eav);

        List<FaceIndex> faceIndices = mapper.scan(FaceIndex.class, scanExpression);
        for(FaceIndex faceIndex : faceIndices){
            logger.info(faceIndex.getName());

        }
    }

    @Test
    public void printjson(){
        String result = "{\n" +
                "  \"Name\" : " + "\"" + "Kim" +"\"" + "\n" +
                "}";

        logger.info(result);
    }

    @Test
    public void jsonTest11() throws IOException {
        String json = "{\n" +
                "    \"InputInformation\": {\n" +
                "        \"KinesisVideo\": {\n" +
                "            \"StreamArn\": \"arn:aws:kinesisvideo:us-west-2:584518143473:stream/boothDemo/1515147770749\",\n" +
                "            \"FragmentNumber\": \"91343852335387790820043054392950742048153326995\",\n" +
                "            \"ServerTimestamp\": 1515593423.375,\n" +
                "            \"ProducerTimestamp\": 1515593419.858,\n" +
                "            \"FrameOffsetInSeconds\": 1\n" +
                "        }\n" +
                "    },\n" +
                "    \"StreamProcessorInformation\": {\n" +
                "        \"Status\": \"RUNNING\"\n" +
                "    },\n" +
                "    \"FaceSearchResponse\": [\n" +
                "        {\n" +
                "            \"DetectedFace\": {\n" +
                "                \"BoundingBox\": {\n" +
                "                    \"Height\": 0.62222224,\n" +
                "                    \"Width\": 0.35,\n" +
                "                    \"Left\": 0.384375,\n" +
                "                    \"Top\": 0.13611111\n" +
                "                },\n" +
                "                \"Confidence\": 99.9832,\n" +
                "                \"Landmarks\": [\n" +
                "                    {\n" +
                "                        \"X\": 0.4952248,\n" +
                "                        \"Y\": 0.3977545,\n" +
                "                        \"Type\": \"eyeLeft\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"X\": 0.6069967,\n" +
                "                        \"Y\": 0.38375852,\n" +
                "                        \"Type\": \"eyeRight\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"X\": 0.5482842,\n" +
                "                        \"Y\": 0.5098527,\n" +
                "                        \"Type\": \"nose\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"X\": 0.51032466,\n" +
                "                        \"Y\": 0.5807056,\n" +
                "                        \"Type\": \"mouthLeft\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"X\": 0.6017903,\n" +
                "                        \"Y\": 0.5784217,\n" +
                "                        \"Type\": \"mouthRight\"\n" +
                "                    }\n" +
                "                ],\n" +
                "                \"Pose\": {\n" +
                "                    \"Pitch\": -14.838173,\n" +
                "                    \"Roll\": -3.2592778,\n" +
                "                    \"Yaw\": -2.6307833\n" +
                "                },\n" +
                "                \"Quality\": {\n" +
                "                    \"Brightness\": 52.8686,\n" +
                "                    \"Sharpness\": 99.998024\n" +
                "                }\n" +
                "            },\n" +
                "            \"MatchedFaces\": [\n" +
                "                {\n" +
                "                    \"Similarity\": 96.701096,\n" +
                "                    \"Face\": {\n" +
                "                        \"BoundingBox\": {\n" +
                "                            \"Height\": 0.46944,\n" +
                "                            \"Width\": 0.708974,\n" +
                "                            \"Left\": 0.121795,\n" +
                "                            \"Top\": 0.173175\n" +
                "                        },\n" +
                "                        \"FaceId\": \"005f37f0-9243-4f64-80a3-31f22256b387\",\n" +
                "                        \"Confidence\": 99.9985,\n" +
                "                        \"ImageId\": \"da9e431f-9a93-5476-883f-85abae7ba482\",\n" +
                "                        \"ExternalImageId\": \"kim-001.png\"\n" +
                "                    }\n" +
                "                },\n" +
                "                {\n" +
                "                    \"Similarity\": 96.701096,\n" +
                "                    \"Face\": {\n" +
                "                        \"BoundingBox\": {\n" +
                "                            \"Height\": 0.46944,\n" +
                "                            \"Width\": 0.708974,\n" +
                "                            \"Left\": 0.121795,\n" +
                "                            \"Top\": 0.173175\n" +
                "                        },\n" +
                "                        \"FaceId\": \"d029476f-ba9a-4752-994e-b835cda61792\",\n" +
                "                        \"Confidence\": 99.9985,\n" +
                "                        \"ImageId\": \"25bbbb91-9c38-5f91-95ad-9d8521c87f2b\",\n" +
                "                        \"ExternalImageId\": \"kim-002.png\"\n" +
                "                    }\n" +
                "                }\n" +
                "            ]\n" +
                "        }\n" +
                "    ]\n" +
                "}";


        try {
            // 讀取寫入的時間戳記, 辨識率, 塞到DDB去
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(json);
            float presentTime = root.path("InputInformation").path("KinesisVideo").findPath("ServerTimestamp").floatValue();
            logger.info("presentTime : {}",presentTime);

            JsonNode matchedFaces = root.path("FaceSearchResponse").findPath("MatchedFaces");

            if(matchedFaces.size()>0){
                logger.info("matched !! ");

                float similarity = matchedFaces.get(0).findPath("Similarity").floatValue();
                logger.info("similartiy : {}",similarity);

                Recognition recognition = new Recognition();
                recognition.setTimestamp(presentTime);
                recognition.setSimilarity(similarity);

                RecognitionPersistence persistence = new RecognitionPersistence();
                persistence.saveRecognition(recognition);
            }

        } catch (IOException e) {
            logger.error(e.getMessage());
        }
        //JsonNode matched = root.path("FaceSearchResponse").findPath("MatchedFaces");
        //assertNull(matched.textValue());
//        float similarity = root.path("FaceSearchResponse").findPath("MatchedFaces").get(0).findPath("Similarity").floatValue();
//        logger.info("similartiy : {}",similarity);
    }
}
