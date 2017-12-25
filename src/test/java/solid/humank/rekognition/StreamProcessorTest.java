package solid.humank.rekognition;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.rekognition.AmazonRekognition;
import com.amazonaws.services.rekognition.model.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import solid.humank.utils.AmazonClientUtil;
import solid.humank.utils.DateTimeUtil;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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


    @Nested
    class CreateProcessor{
        @Test
        public void create_stream_processor_test(){

            init();

            RekognitionService rekognitionService = new RekognitionService();
            String createdProcessorArn =
                    rekognitionService.createVideoStreamProcessor(kinesisVideoStreamArn, kinesisDataStreamArn, collectionId, matchThreshold, roleArn, streamProcessorName);
            assertNotNull(createdProcessorArn);
        }

        @Nested
        class RetrieveProcessor{

            //list

            @Test
            public void list_stream_processors(){
                AWSCredentials credentials=  AmazonClientUtil.generateCredentials();
                AmazonRekognition rekognitionClient =  AmazonClientUtil.getAmazonRekognition(Regions.US_WEST_2,credentials);
                ListStreamProcessorsResult listStreamProcessorsResult =
                        rekognitionClient.listStreamProcessors(new ListStreamProcessorsRequest().withMaxResults(100));
                for (StreamProcessor streamProcessor : listStreamProcessorsResult.getStreamProcessors()) {
                    logger.info("StreamProcessor name - {}" , streamProcessor.getName());
                    logger.info("Status - {}" , streamProcessor.getStatus());
                }
            }

            //start

            @Test
            public void start_stream_processor_test(){

                RekognitionService rekognitionService = new RekognitionService();
                String result = rekognitionService.startStreamProcessor(streamProcessorName);
                logger.info(result);

            }

            //describe

            @Test
            public void describe_stream_processor_test(){
                AWSCredentials credentials=  AmazonClientUtil.generateCredentials();
                AmazonRekognition rekognitionClient =  AmazonClientUtil.getAmazonRekognition(Regions.US_WEST_2,credentials);
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
            class StopProcessor{
                @Test
                public void stop_stream_rpocessor_test(){
                    AWSCredentials credentials=  AmazonClientUtil.generateCredentials();
                    AmazonRekognition rekognitionClient =  AmazonClientUtil.getAmazonRekognition(Regions.US_WEST_2,credentials);
                    StopStreamProcessorResult stopStreamProcessorResult =
                            rekognitionClient.stopStreamProcessor(new StopStreamProcessorRequest().withName(streamProcessorName));
                    logger.info("{}", stopStreamProcessorResult.getSdkResponseMetadata().toString());
                }

                @Nested
                class DeleteProcessor{
                    @Test
                    public void delete_stream_processor_test(){
                        AWSCredentials credentials=  AmazonClientUtil.generateCredentials();
                        AmazonRekognition rekognitionClient =  AmazonClientUtil.getAmazonRekognition(Regions.US_WEST_2,credentials);
                        DeleteStreamProcessorResult deleteStreamProcessorResult = rekognitionClient
                                .deleteStreamProcessor(new DeleteStreamProcessorRequest().withName(streamProcessorName));

                        logger.info("{}", deleteStreamProcessorResult.getSdkResponseMetadata().toString());
                    }

                    @Nested
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
