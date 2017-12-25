package solid.humank.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.S3Event;

//TODO : 透過讀取config找到 bucketname --> rekognition-index , 把上傳進來的檔案做index, 並且從tag找出人名, 寫入dynamodb

public class FaceIndexHandler implements RequestHandler<S3Event, String> {
    @Override
    public String handleRequest(S3Event s3Event, Context context) {
        return null;
    }
}
