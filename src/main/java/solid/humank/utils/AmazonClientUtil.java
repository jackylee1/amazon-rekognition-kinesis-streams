package solid.humank.utils;

import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Regions;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.rekognition.AmazonRekognition;
import com.amazonaws.services.rekognition.AmazonRekognitionClientBuilder;

public class AmazonClientUtil {
//    public static AWSCredentials generateCredentials() {
//        try {
//            AWSCredentials credentials = new ProfileCredentialsProvider().getCredentials();
//            return credentials;
//        } catch (Exception e) {
//            throw new AmazonClientException(
//                    "Cannot load the credentials from the credential profiles file. " +
//                            "Please make sure that your credentials file is at the correct " +
//                            "location (/Users/userid/.aws/credentials), and is in valid format.",
//                    e);
//        }
//    }

//    public static AmazonRekognition getAmazonRekognition(Regions region, AWSCredentials credentials) {
//
//        return AmazonRekognitionClientBuilder.defaultClient();
//
    public static AmazonRekognition getAmazonRekognition() {

            return AmazonRekognitionClientBuilder.defaultClient();
//        return AmazonRekognitionClientBuilder
//                .standard()
//                .withRegion(region)
//                .withCredentials(new AWSStaticCredentialsProvider(credentials))
//                .build();
    }

    public static AmazonDynamoDB getAmazonDynamoDBClient(){
        return AmazonDynamoDBClientBuilder.defaultClient();
    }
}
