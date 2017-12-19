package solid.humank.utils;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class DateTimeUtil {

    public static String getCurrentDateTimeInYMDHMS() {
        DateTime dt = new DateTime();
        DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyyMMddHHmmss");
        return dt.toString(formatter);
    }

}
