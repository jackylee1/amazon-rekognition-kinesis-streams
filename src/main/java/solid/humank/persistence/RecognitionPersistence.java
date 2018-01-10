package solid.humank.persistence;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.PutItemOutcome;
import com.amazonaws.services.dynamodbv2.document.Table;
import solid.humank.model.Recognition;
import solid.humank.utils.AmazonClientUtil;

public class RecognitionPersistence {

    public String saveRecognition(Recognition recognition) {

        AmazonDynamoDB client = AmazonClientUtil.getAmazonDynamoDBClient();
        DynamoDB dynamoDB = new DynamoDB(client);

        Table recognitionTable = dynamoDB.getTable("recognition");

        Item item = new Item()
                .withPrimaryKey("timestamp", recognition.getTimestamp())
                .withFloat("similarity", recognition.getSimilarity());
        PutItemOutcome outcome = recognitionTable.putItem(item);
        return outcome.toString();
    }
}
