package solid.humank.persistence;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.PutItemOutcome;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.rekognition.model.FaceRecord;
import solid.humank.utils.AmazonClientUtil;
import solid.humank.utils.ResourceProperties;

import java.util.List;

public class FaceIndexPersistence {

    public String saveBuddy(String buddyName, List<FaceRecord> faceRecords){

        AmazonDynamoDB client = AmazonClientUtil.getAmazonDynamoDBClient();
        StringBuilder sb = new StringBuilder();

        DynamoDB dynamoDB = new DynamoDB(client);
        Table faceIndexTable = dynamoDB.getTable("faceindex");

        for(FaceRecord faceRecord : faceRecords){
            Item item = new Item()
                    .withPrimaryKey("name", buddyName)
                    .withString("faceRecord",faceRecord.toString());
            PutItemOutcome outcome = faceIndexTable.putItem(item);
            sb.append(outcome.toString());
        }
        return sb.toString();
    }
}
