package solid.humank.rekognition;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.rekognition.AmazonRekognition;
import com.amazonaws.services.rekognition.model.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import solid.humank.service.CollectionService;
import solid.humank.service.RekognitionService;
import solid.humank.utils.AmazonClientUtil;
import solid.humank.utils.DateTimeUtil;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Disabled
@Nested
public class StreamProcessorTest {

    private String streamProcessorName ="myVideoStreamProcessor";
    private String kinesisVideoStreamArn = "arn:aws:kinesisvideo:us-west-2:584518143473:stream/MyKinesisVideoStream/1513753760664";
    private String kinesisDataStreamArn = "arn:aws:kinesis:us-west-2:584518143473:stream/MyKinesisDataStream";
    private String roleArn = "arn:aws:iam::584518143473:role/rekognition-role";

    private float matchThreshold = 85.5f;

    private static final Logger logger = LogManager.getLogger();
    static String currentDateTime;
    static String collectionId = "myCollection";


    public static void init(){
        currentDateTime = DateTimeUtil.getCurrentDateTimeInYMDHMS();
        collectionId = collectionId.concat(currentDateTime);

        CollectionService cc = new CollectionService();
        String creationResult = cc.createCollectionAt(Regions.US_WEST_2,collectionId);
        logger.info("The created collection id is : {}", collectionId);
        assertEquals("200",creationResult);

        logger.info("init running.");
    }


    @Disabled
    @Nested
    class CreateProcessor{
        @Disabled
        @Test
        public void create_stream_processor_test(){

            init();

            RekognitionService rekognitionService = new RekognitionService();
            String createdProcessorArn =
                    rekognitionService.createVideoStreamProcessor(kinesisVideoStreamArn, kinesisDataStreamArn, collectionId, matchThreshold, roleArn, streamProcessorName);
            assertNotNull(createdProcessorArn);
        }

        @Nested
        @Disabled
        class RetrieveProcessor{

            //list

            @Test
            @Disabled
            public void list_stream_processors(){

                AmazonRekognition rekognitionClient =  AmazonClientUtil.getAmazonRekognition();
                ListStreamProcessorsResult listStreamProcessorsResult =
                        rekognitionClient.listStreamProcessors(new ListStreamProcessorsRequest().withMaxResults(100));
                for (StreamProcessor streamProcessor : listStreamProcessorsResult.getStreamProcessors()) {
                    logger.info("StreamProcessor name - {}" , streamProcessor.getName());
                    logger.info("Status - {}" , streamProcessor.getStatus());
                }
            }

            //start

            @Test
            @Disabled
            public void start_stream_processor_test(){

                RekognitionService rekognitionService = new RekognitionService();
                String result = rekognitionService.startStreamProcessor(streamProcessorName);
                logger.info(result);

            }

            //describe

            @Test
            @Disabled
            public void describe_stream_processor_test(){

                AmazonRekognition rekognitionClient =  AmazonClientUtil.getAmazonRekognition();
                DescribeStreamProcessorResult describeStreamProcessorResult = rekognitionClient.describeStreamProcessor(new DescribeStreamProcessorRequest().withName(streamProcessorName));

                logger.info("Arn - {}", describeStreamProcessorResult.getStreamProcessorArn());
                logger.info("Input kinesisVideo stream - {}" , describeStreamProcessorResult.getInput().getKinesisVideoStream().getArn());
                logger.info("Output kinesisData stream - {}" , describeStreamProcessorResult.getOutput().getKinesisDataStream().getArn());
                logger.info("RoleArn - {}",  describeStreamProcessorResult.getRoleArn());
                logger.info("CollectionId - {}" , describeStreamProcessorResult.getSettings().getFaceSearch().getCollectionId());
                logger.info("Status - {}" , describeStreamProcessorResult.getStatus());
                logger.info("Status message - {}" , describeStreamProcessorResult.getStatusMessage());
                logger.info("Creation timestamp - {}" , describeStreamProcessorResult.getCreationTimestamp());
                logger.info("Last update timestamp - {}" , describeStreamProcessorResult.getLastUpdateTimestamp());

            }

            @Nested
            @Disabled
            class StopProcessor{
                @Test
                public void stop_stream_rpocessor_test(){

                    AmazonRekognition rekognitionClient =  AmazonClientUtil.getAmazonRekognition();
                    StopStreamProcessorResult stopStreamProcessorResult =
                            rekognitionClient.stopStreamProcessor(new StopStreamProcessorRequest().withName(streamProcessorName));
                    logger.info("{}", stopStreamProcessorResult.getSdkResponseMetadata().toString());
                }

                @Nested
                @Disabled
                class DeleteProcessor{
                    @Test
                    @Disabled
                    public void delete_stream_processor_test(){

                        AmazonRekognition rekognitionClient =  AmazonClientUtil.getAmazonRekognition();
                        DeleteStreamProcessorResult deleteStreamProcessorResult = rekognitionClient
                                .deleteStreamProcessor(new DeleteStreamProcessorRequest().withName(streamProcessorName));

                        logger.info("{}", deleteStreamProcessorResult.getSdkResponseMetadata().toString());
                    }

                    @Nested
                    @Disabled
                    class DeleteCollection{

                        public void delete_collection(){
                            CollectionService cc = new CollectionService();
                            String deletionResult = cc.deleteCollection(Regions.US_WEST_2,collectionId);
                            assertEquals("200",deletionResult);
                        }
                    }
                }
            }
        }
    }
}
