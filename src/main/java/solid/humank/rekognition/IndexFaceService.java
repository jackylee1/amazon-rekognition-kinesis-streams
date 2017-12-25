package solid.humank.rekognition;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.rekognition.AmazonRekognition;
import com.amazonaws.services.rekognition.model.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import solid.humank.utils.AmazonClientUtil;

import java.util.List;

public class IndexFaceService {

    private static final Logger logger = LogManager.getLogger();

    public List<FaceRecord> indexFaceFor(Regions region, String collectionId, String s3BucketName, String fileName) {

        AWSCredentials awsCredentials = AmazonClientUtil.generateCredentials();
        AmazonRekognition amazonRekognition = AmazonClientUtil.getAmazonRekognition(region,awsCredentials);

        Image image=new Image()
                .withS3Object(new S3Object()
                        .withBucket(s3BucketName)
                        .withName(fileName));

        IndexFacesRequest indexFacesRequest = new IndexFacesRequest()
                .withImage(image)
                .withCollectionId(collectionId)
                .withExternalImageId(fileName)
                .withDetectionAttributes("ALL");

        IndexFacesResult indexFacesResult=amazonRekognition.indexFaces(indexFacesRequest);

        logger.info("{} added", fileName);

        List < FaceRecord > faceRecords = indexFacesResult.getFaceRecords();
        for (FaceRecord faceRecord: faceRecords) {
            logger.info("Face detected: FaceId is : {}" ,faceRecord.getFace().getFaceId());
        }

        return faceRecords;
    }
}
