package solid.humank.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.KinesisEvent;

//TODO : 完成從Kinesis Data stream 得到的資訊來解釋辨識結果 http://docs.aws.amazon.com/zh_cn/lambda/latest/dg/with-kinesis-example-deployment-pkg.html
//TODO : 分析結果 http://docs.aws.amazon.com/zh_cn/streams/latest/dev/kinesis-record-processor-implementation-app-java.html

public class KinesisDataStreamHandler implements RequestHandler<KinesisEvent, Void> {
    @Override
    public Void handleRequest(KinesisEvent kinesisEvent, Context context) {
        for(KinesisEvent.KinesisEventRecord rec : kinesisEvent.getRecords()) {
            System.out.println(new String(rec.getKinesis().getData().array()));
        }

        return null;
    }
}
