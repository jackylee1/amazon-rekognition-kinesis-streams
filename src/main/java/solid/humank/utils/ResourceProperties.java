package solid.humank.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.Properties;

public class ResourceProperties {

    private static Logger logger = LogManager.getLogger();
    private static Properties properties;
    static{
        try {
             properties = new Properties();
            properties.load(ResourceProperties.class.getResourceAsStream("/Resource.properties"));
        } catch (IOException e) {
            logger.error(e.getMessage());

        }
    }

    public static String getPropertyValue(String property){
        return properties.getProperty(property);
    }

}
