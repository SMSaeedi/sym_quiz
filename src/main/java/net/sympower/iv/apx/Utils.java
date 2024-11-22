package net.sympower.iv.apx;

import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.Date;
import java.util.TimeZone;

public class Utils {
    public static String formatDate(long epochMillis) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf.setTimeZone(TimeZone.getTimeZone(ZoneId.of("Europe/Amsterdam").normalized()));
        return sdf.format(new Date(epochMillis));
    }

    public static String getValueForLabel(Quote quote, String label) {
        return quote.getValues().stream()
                .filter(value -> label.equals(value.getTLabel()))
                .findFirst()
                .map(Value::getValue)
                .orElse("N/A");
    }
}
