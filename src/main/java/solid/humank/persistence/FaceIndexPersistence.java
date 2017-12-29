package solid.humank.persistence;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.PutItemOutcome;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.rekognition.model.FaceRecord;
import solid.humank.model.FaceIndex;
import solid.humank.utils.AmazonClientUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FaceIndexPersistence {

    public String saveBuddy(String buddyName, List<FaceRecord> faceRecords){

        AmazonDynamoDB client = AmazonClientUtil.getAmazonDynamoDBClient();
        DynamoDB dynamoDB = new DynamoDB(client);

        StringBuilder sb = new StringBuilder();
        Table faceIndexTable = dynamoDB.getTable("faceindex");

        for(FaceRecord faceRecord : faceRecords){
            Item item = new Item()
                    .withPrimaryKey("name", buddyName)
                    .withString("faceId",faceRecord.getFace().getFaceId())
                    .withString("faceRecord",faceRecord.toString());
            PutItemOutcome outcome = faceIndexTable.putItem(item);
            sb.append(outcome.toString());
        }
        return sb.toString();
    }

    public String findBuddyForFaceId(String faceId){

        String name ="NOT_FOUND";

        AmazonDynamoDB client = AmazonClientUtil.getAmazonDynamoDBClient();
        DynamoDBMapper mapper = new DynamoDBMapper(client);

        Map<String, AttributeValue> eav = new HashMap<String, AttributeValue>();
        eav.put(":val1", new AttributeValue().withS(faceId));
        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression()
                .withFilterExpression("faceId = :val1").withExpressionAttributeValues(eav);

        List<FaceIndex> faceIndices = mapper.scan(FaceIndex.class, scanExpression);

        if(faceIndices!=null){
            name = faceIndices.get(0).getName();
        }

        return name;

    }
}
