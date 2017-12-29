package solid.humank.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

@DynamoDBTable(tableName="faceindex")
public class FaceIndex {

    private String name;
    private String faceId;
    private String faceRecord;

    @DynamoDBHashKey(attributeName="name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @DynamoDBAttribute(attributeName="faceId")
    public String getFaceId() {
        return faceId;
    }

    public void setFaceId(String faceId) {
        this.faceId = faceId;
    }

    @DynamoDBAttribute(attributeName="faceRecord")
    public String getFaceRecord() {
        return faceRecord;
    }

    public void setFaceRecord(String faceRecord) {
        this.faceRecord = faceRecord;
    }
}
