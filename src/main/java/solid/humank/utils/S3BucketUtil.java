package solid.humank.utils;

import com.amazonaws.services.lambda.runtime.events.S3Event;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GetObjectTaggingRequest;
import com.amazonaws.services.s3.model.GetObjectTaggingResult;
import com.amazonaws.services.s3.model.Tag;

public class S3BucketUtil {

    public static String getBuddyName(String indexBucket, String objectKey, AmazonS3 s3client) {
        GetObjectTaggingRequest getTaggingRequest = new GetObjectTaggingRequest(indexBucket, objectKey);
        GetObjectTaggingResult getTagsResult = s3client.getObjectTagging(getTaggingRequest);

        String incomingBuddyName=null;
        for(Tag tag : getTagsResult.getTagSet()){
            if(("name".equals(tag.getKey()))){
                incomingBuddyName = tag.getValue();
            }
        }
        return incomingBuddyName;
    }

    public static String getObjectKey(S3Event s3Event) {
        return s3Event.getRecords().get(0).getS3().getObject().getKey();
    }

    public static String getIndexBucket(S3Event s3Event) {
        return s3Event.getRecords().get(0).getS3().getBucket().getName();
    }
}
