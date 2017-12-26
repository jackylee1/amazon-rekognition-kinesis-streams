package solid.humank.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.S3Event;
import com.amazonaws.services.rekognition.model.FaceRecord;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.GetObjectTaggingRequest;
import com.amazonaws.services.s3.model.GetObjectTaggingResult;
import com.amazonaws.services.s3.model.Tag;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import solid.humank.persistence.FaceIndexPersistence;
import solid.humank.service.IndexFaceService;
import solid.humank.utils.ResourceProperties;

import java.util.List;

import static solid.humank.utils.S3BucketUtil.getBuddyName;
import static solid.humank.utils.S3BucketUtil.getIndexBucket;
import static solid.humank.utils.S3BucketUtil.getObjectKey;

//TODO : 透過讀取config找到 bucketname --> rekognition-index , 把上傳進來的檔案做index, 並且從tag找出人名, 寫入dynamodb

public class FaceIndexHandler implements RequestHandler<S3Event, String> {

    private static final Logger logger = LogManager.getLogger();

    @Override
    public String handleRequest(S3Event s3Event, Context context) {

        AmazonS3 s3client = AmazonS3ClientBuilder.standard().build();

        String indexBucket = getIndexBucket(s3Event);
        String objectKey = getObjectKey(s3Event);
        String incomingBuddyName =  getBuddyName(indexBucket, objectKey, s3client);
        //String collectionId = ResourceProperties.getPropertyValue("collectionId");
        String collectionId = "myCollection";
        logger.info("collectionId from resource :  {}", collectionId);
        context.getLogger().log("the collectionId from resource :" + collectionId);

        String saveResult = indexForIncomingBuddy(indexBucket, objectKey, collectionId,incomingBuddyName);

        return saveResult;
    }

    private String indexForIncomingBuddy(String indexBucket, String objectKey, String collectionId, String incomingBuddyName) {

        IndexFaceService indexFaceService = new IndexFaceService();
        List<FaceRecord> faceRecords = indexFaceService.indexFaceFor(collectionId, indexBucket, objectKey);
        FaceIndexPersistence persistence = new FaceIndexPersistence();
        String putOutCome = persistence.saveBuddy(incomingBuddyName, faceRecords);

        return putOutCome;
    }



}
