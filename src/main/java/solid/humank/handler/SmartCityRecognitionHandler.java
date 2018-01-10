package solid.humank.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.KinesisEvent;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import solid.humank.model.Recognition;
import solid.humank.persistence.RecognitionPersistence;

import java.io.IOException;

public class SmartCityRecognitionHandler implements RequestHandler<KinesisEvent, Void> {

    private static final Logger logger = LogManager.getLogger();

    @Override
    public Void handleRequest(KinesisEvent kinesisEvent, Context context) {
        for(KinesisEvent.KinesisEventRecord rec : kinesisEvent.getRecords()) {
            String data = new String(rec.getKinesis().getData().array());

            logger.info("record : {}", data);
            try {
                // 讀取寫入的時間戳記, 辨識率, 塞到DDB去
                ObjectMapper mapper = new ObjectMapper();
                JsonNode root = mapper.readTree(data);
                float presentTime = root.path("InputInformation").path("KinesisVideo").findPath("ServerTimestamp").floatValue();
                logger.info("presentTime : {}",presentTime);

                JsonNode matchedFaces = root.path("FaceSearchResponse").findPath("MatchedFaces");
                if(matchedFaces.size()>0){
                    logger.info("matched !! ");

                    float similarity = matchedFaces.get(0).findPath("Similarity").floatValue();
                    logger.info("similartiy : {}",similarity);

                    Recognition recognition = new Recognition();
                    recognition.setTimestamp(presentTime);
                    recognition.setSimilarity(similarity);

                    RecognitionPersistence persistence = new RecognitionPersistence();
                    persistence.saveRecognition(recognition);
                }

            } catch (IOException e) {
                logger.error(e.getMessage());
            }
        }
        return null;
    }
}
