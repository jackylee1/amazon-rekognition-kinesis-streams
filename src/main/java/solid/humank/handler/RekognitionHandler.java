package solid.humank.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.S3Event;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class RekognitionHandler implements RequestHandler<S3Event, String> {

    private static final Logger logger = LogManager.getLogger();
    @Override
    public String handleRequest(S3Event event, Context context) {

        //TODO : accept the incoming buddy inform which is new pic in s3 bucket, s3 trigger the lambda

        AmazonS3 s3client = AmazonS3ClientBuilder.standard().build();

        context.getLogger().log("Received event: " + event);

        // Get the object from the event and show its content type
        String bucket = event.getRecords().get(0).getS3().getBucket().getName();
        String key = event.getRecords().get(0).getS3().getObject().getKey();


        GetObjectTaggingRequest getTaggingRequest = new GetObjectTaggingRequest(bucket, key);
        GetObjectTaggingResult getTagsResult = s3client.getObjectTagging(getTaggingRequest);

        for(Tag tag : getTagsResult.getTagSet()){
            tag.getKey();
            tag.getValue();
        }


        try {
            S3Object response = s3client.getObject(new GetObjectRequest(bucket, key));
            String contentType = response.getObjectMetadata().getContentType();
            context.getLogger().log("CONTENT TYPE: " + contentType);
            return contentType;
        } catch (Exception e) {
            logger.error(e.getMessage());
            context.getLogger().log(String.format(
                    "Error getting object %s from bucket %s. Make sure they exist and"
                            + " your bucket is in the same region as this function.", key, bucket));
            throw e;
        }
    }
}