package solid.humank.rekognition;

import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.rekognition.AmazonRekognition;
import com.amazonaws.services.rekognition.AmazonRekognitionClientBuilder;
import com.amazonaws.services.rekognition.model.CreateCollectionRequest;
import com.amazonaws.services.rekognition.model.CreateCollectionResult;
import com.amazonaws.services.rekognition.model.DeleteCollectionRequest;
import com.amazonaws.services.rekognition.model.DeleteCollectionResult;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CollectionController {

    private static final Logger logger = LogManager.getLogger();

    public String createCollectionAt(Regions region, String collectionId) {

        AWSCredentials credentials =  generateCredentials();
        AmazonRekognition amazonRekognition = getAmazonRekognition(region, credentials);

        logger.info("Creating collection: {}" ,collectionId );

        CreateCollectionRequest request = new CreateCollectionRequest()
                .withCollectionId(collectionId);

        CreateCollectionResult createCollectionResult = amazonRekognition.createCollection(request);
        logger.info("CollectionArn : {} " ,createCollectionResult.getCollectionArn());
        logger.info("Status code : {}" , createCollectionResult.getStatusCode().toString());

        return createCollectionResult.getStatusCode().toString();
    }


    public String deleteCollection(Regions region, String collectionId){

        AWSCredentials credentials =  generateCredentials();
        AmazonRekognition amazonRekognition = getAmazonRekognition(region, credentials);

        DeleteCollectionRequest request = new DeleteCollectionRequest()
                .withCollectionId(collectionId);
        DeleteCollectionResult deleteCollectionResult = amazonRekognition.deleteCollection(request);

        logger.info(collectionId + ": {}" , deleteCollectionResult.getStatusCode().toString());
        return deleteCollectionResult.getStatusCode().toString();
    }

    private AWSCredentials generateCredentials() {
        try {
            AWSCredentials credentials = new ProfileCredentialsProvider().getCredentials();
            return credentials;
        } catch (Exception e) {
            throw new AmazonClientException(
                    "Cannot load the credentials from the credential profiles file. " +
                            "Please make sure that your credentials file is at the correct " +
                            "location (/Users/userid/.aws/credentials), and is in valid format.",
                    e);
        }
    }

    private AmazonRekognition getAmazonRekognition(Regions region, AWSCredentials credentials) {
        return AmazonRekognitionClientBuilder
                .standard()
                .withRegion(region)
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .build();
    }

}
