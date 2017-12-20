package solid.humank.rekognition;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class StreamProcessorTest {

    private String streamProcessorName;
    private String kinesisVideoStreamArn;
    private String kinesisDataStreamArn;
    private String roleArn;
    private String collectionId;
    private float matchThreshold;

    @Test
    public void create_stream_processor_test(){

        KinesisStreamService kinesisStreamService = new KinesisStreamService();
        String createdProcessorArn =
                kinesisStreamService.createVideoStreamProcessor(kinesisVideoStreamArn, kinesisDataStreamArn, collectionId, matchThreshold, roleArn, streamProcessorName);
        assertNotNull(createdProcessorArn);
    }

    @Test
    public void start_stream_processor_test(){

    }

    @Test
    public void stop_stream_rpocessor_test(){

    }

    @Test
    public void delete_stream_processor_test(){

    }

    @Test
    public void describe_stream_processor_test(){

    }

    @Test
    public void list_stream_processors(){

    }
}
