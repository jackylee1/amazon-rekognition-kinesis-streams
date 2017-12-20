package solid.humank.rekognition;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.rekognition.AmazonRekognition;
import com.amazonaws.services.rekognition.model.CreateCollectionRequest;
import com.amazonaws.services.rekognition.model.CreateCollectionResult;
import com.amazonaws.services.rekognition.model.DeleteCollectionRequest;
import com.amazonaws.services.rekognition.model.DeleteCollectionResult;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import solid.humank.utils.AmazonClientUtil;

public class CollectionService {

    private static final Logger logger = LogManager.getLogger();

    public String createCollectionAt(Regions region, String collectionId) {

        AWSCredentials credentials =  AmazonClientUtil.generateCredentials();
        AmazonRekognition amazonRekognition = AmazonClientUtil.getAmazonRekognition(region, credentials);

        logger.info("Creating collection: {}" ,collectionId );

        CreateCollectionRequest request = new CreateCollectionRequest()
                .withCollectionId(collectionId);

        CreateCollectionResult createCollectionResult = amazonRekognition.createCollection(request);
        logger.info("CollectionArn : {} " ,createCollectionResult.getCollectionArn());
        logger.info("Status code : {}" , createCollectionResult.getStatusCode().toString());

        return createCollectionResult.getStatusCode().toString();
    }


    public String deleteCollection(Regions region, String collectionId){

        AWSCredentials credentials =  AmazonClientUtil.generateCredentials();
        AmazonRekognition amazonRekognition = AmazonClientUtil.getAmazonRekognition(region, credentials);

        DeleteCollectionRequest request = new DeleteCollectionRequest()
                .withCollectionId(collectionId);
        DeleteCollectionResult deleteCollectionResult = amazonRekognition.deleteCollection(request);

        logger.info(collectionId + ": {}" , deleteCollectionResult.getStatusCode().toString());
        return deleteCollectionResult.getStatusCode().toString();
    }

}
