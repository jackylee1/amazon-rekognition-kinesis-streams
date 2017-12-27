package solid.humank.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.S3Event;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sns.AmazonSNSClientBuilder;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.PublishResult;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import solid.humank.service.RekognitionService;
import solid.humank.utils.ResourceProperties;

import static solid.humank.utils.S3BucketUtil.getBuddyName;
import static solid.humank.utils.S3BucketUtil.getIndexBucket;
import static solid.humank.utils.S3BucketUtil.getObjectKey;

public class RekognitionHandler implements RequestHandler<S3Event, String> {

    private static final String FACE_DETECT_ARN = "arn:aws:sns:us-west-2:584518143473:face-detect";
    private static final String COLLECTION_ID = "myCollection";

    private static final Logger logger = LogManager.getLogger();
    @Override
    public String handleRequest(S3Event s3Event, Context context) {

        AmazonS3 s3client = AmazonS3ClientBuilder.standard().build();

        //這裡是從rekognition-lab 讀圖檔回來
        String s3BucketName = getIndexBucket(s3Event);
        logger.info("s3BucketName is : {}", s3BucketName);

        String objectKey = getObjectKey(s3Event);
        logger.info("InComing Buddy image ObjectKey : {}", objectKey);

        RekognitionService rekognitionService = new RekognitionService();

        String newCollection = ResourceProperties.getPropertyValue("collectionId");
        String result = rekognitionService.compareWithIndexedFacesForIncomingBuddy(s3BucketName,objectKey,newCollection);
        //String result = rekognitionService.compareWithIndexedFacesForIncomingBuddy(s3BucketName,objectKey,COLLECTION_ID);
        logger.info("detect result : {}", result);

        //把辨識結果丟進SNS
        AmazonSNS snsClient = AmazonSNSClientBuilder.defaultClient();
        PublishRequest publishRequest = new PublishRequest(FACE_DETECT_ARN, result);
        PublishResult publishResult = snsClient.publish(publishRequest);
        logger.info("publishResult : {}",publishResult);
        return publishResult.toString();
    }
}