package utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DateUtils {

    public static String getData(Integer data) {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T10':HH:mm.SSS'Z'");
        calendar.add(Calendar.DATE, data);
        return df.format(calendar.getTime());
    }
}
