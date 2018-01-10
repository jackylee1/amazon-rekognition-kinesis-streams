package solid.humank.service;

import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.kinesis.clientlibrary.exceptions.InvalidStateException;
import com.amazonaws.services.kinesis.clientlibrary.exceptions.ShutdownException;
import com.amazonaws.services.kinesis.clientlibrary.exceptions.ThrottlingException;
import com.amazonaws.services.kinesis.clientlibrary.interfaces.IRecordProcessor;
import com.amazonaws.services.kinesis.clientlibrary.interfaces.IRecordProcessorCheckpointer;
import com.amazonaws.services.kinesis.clientlibrary.lib.worker.ShutdownReason;
import com.amazonaws.services.kinesis.model.Record;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sns.AmazonSNSClientBuilder;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.PublishResult;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import solid.humank.persistence.FaceIndexPersistence;
import solid.humank.utils.ResourceProperties;

import javax.annotation.Resource;
import java.io.IOException;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.List;

public class DetectedDataRecordProcessor implements IRecordProcessor {

    private static final Logger logger = LogManager.getLogger();
    private String kinesisShardId;

    // Backoff and retry settings
    private static final long BACKOFF_TIME_IN_MILLIS = 3000L;
    private static final int NUM_RETRIES = 10;

    // Checkpoint about once a minute
    private static final long CHECKPOINT_INTERVAL_MILLIS = 60000L;
    private long nextCheckpointTimeInMillis;

    private final CharsetDecoder decoder = Charset.forName("UTF-8").newDecoder();

    @Override
    public void initialize(String kinesisShardId) {
        logger.info("Initializing record processor for shard: " + kinesisShardId);
        this.kinesisShardId = kinesisShardId;
    }

    @Override
    public void processRecords(List<Record> records, IRecordProcessorCheckpointer iRecordProcessorCheckpointer) {
        logger.info("Processing " + records.size() + " records from " + kinesisShardId);

        // Process records and perform all exception handling.
        processRecordsWithRetries(records);

        // Checkpoint once every checkpoint interval.
        if (System.currentTimeMillis() > nextCheckpointTimeInMillis) {
            checkpoint(iRecordProcessorCheckpointer);
            nextCheckpointTimeInMillis = System.currentTimeMillis() + CHECKPOINT_INTERVAL_MILLIS;
        }
    }

    /**
     * Process records performing retries as needed. Skip "poison pill" records.
     *
     * @param records Data records to be processed.
     */
    private void processRecordsWithRetries(List<Record> records) {
        for (Record record : records) {
            boolean processedSuccessfully = false;
            for (int i = 0; i < NUM_RETRIES; i++) {
                try {
                    processSingleRecord(record);
                    processedSuccessfully = true;
                    break;
                } catch (Throwable t) {
                    logger.warn("Caught throwable while processing record " + record, t);
                }

                // backoff if we encounter an exception.
                try {
                    Thread.sleep(BACKOFF_TIME_IN_MILLIS);
                } catch (InterruptedException e) {
                    logger.debug("Interrupted sleep", e);
                }
            }

            if (!processedSuccessfully) {
                logger.error("Couldn't process record " + record + ". Skipping the record.");
            }
        }
    }

    /**
     * Process a single record.
     *
     * @param record The record to be processed.
     */
    private void processSingleRecord(Record record) {

        String data = null;
        String snsArn = ResourceProperties.getPropertyValue("snsArn");
        String result=null;

        try {
            // For this app, we interpret the payload as UTF-8 chars.
            data = decoder.decode(record.getData()).toString();
            logger.info("Detect Result : {}", data);

            if(data!=null){
                ObjectMapper mapper = new ObjectMapper();
                JsonNode root = mapper.readTree(data);
                JsonNode matchedFaces = root.path("FaceSearchResponse").findPath("MatchedFaces");
                logger.info("matchedFaces : {}",matchedFaces.toString());

                if(matchedFaces!= null){
                    for(int i =0; i<matchedFaces.size();i++){
                        JsonNode detail = matchedFaces.get(i);
                        String faceId = detail.path("Face").findPath("FaceId").textValue();
                        logger.info("FaceId is : {}", faceId);

                        String name = new FaceIndexPersistence().findBuddyForFaceId(faceId);
                        logger.info("name is : {}",name);
                        result = "{\n" +
                                "  \"Name\" : " + "\"" + name +"\"" + "\n" +
                                "}";
                        logger.info("result is : {}", result);
                        AmazonSNS snsClient = AmazonSNSClient.builder()
                                .withRegion("ap-northeast-1")
                                .build();
                        PublishRequest publishRequest = new PublishRequest(snsArn, result);

                        logger.info("snsArn is : {}",snsArn);
                        PublishResult publishResult = snsClient.publish(publishRequest);
                        logger.info("publishResult : {}",publishResult);
                    }
                }
            }

        } catch (CharacterCodingException e) {
            logger.error("Malformed data: " + data, e);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void shutdown(IRecordProcessorCheckpointer iRecordProcessorCheckpointer, ShutdownReason shutdownReason) {
        logger.info("Shutting down record processor for shard: " + kinesisShardId);
        // Important to checkpoint after reaching end of shard, so we can start processing data from child shards.
        if (shutdownReason == ShutdownReason.TERMINATE) {
            checkpoint(iRecordProcessorCheckpointer);
        }
    }

    /** Checkpoint with retries.
     * @param checkpointer
     */
    private void checkpoint(IRecordProcessorCheckpointer checkpointer) {
        logger.info("Checkpointing shard " + kinesisShardId);
        for (int i = 0; i < NUM_RETRIES; i++) {
            try {
                checkpointer.checkpoint();
                break;
            } catch (ShutdownException se) {
                // Ignore checkpoint if the processor instance has been shutdown (fail over).
                logger.info("Caught shutdown exception, skipping checkpoint.", se);
                break;
            } catch (ThrottlingException e) {
                // Backoff and re-attempt checkpoint upon transient failures
                if (i >= (NUM_RETRIES - 1)) {
                    logger.error("Checkpoint failed after " + (i + 1) + "attempts.", e);
                    break;
                } else {
                    logger.info("Transient issue when checkpointing - attempt " + (i + 1) + " of "
                            + NUM_RETRIES, e);
                }
            } catch (InvalidStateException e) {
                // This indicates an issue with the DynamoDB table (check for table, provisioned IOPS).
                logger.error("Cannot save checkpoint to the DynamoDB table used by the Amazon Kinesis Client Library.", e);
                break;
            }
            try {
                Thread.sleep(BACKOFF_TIME_IN_MILLIS);
            } catch (InterruptedException e) {
                logger.debug("Interrupted sleep", e);
            }
        }
    }
}