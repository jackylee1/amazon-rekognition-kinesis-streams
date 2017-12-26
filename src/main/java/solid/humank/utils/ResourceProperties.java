package solid.humank.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.Properties;

public class ResourceProperties {

    private static Logger logger = LogManager.getLogger();

    public static String getPropertyValue(String property){

        Properties properties = new Properties();
        try {
            properties.load(ResourceProperties.class.getResourceAsStream("/resources/Resource.properties"));
        } catch (IOException e) {

            e.printStackTrace();
        }

        return properties.getProperty(property);
    }

}
