package fr.funixgaming.twitch.api;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;

public class TwitchResources {
    private final static SimpleDateFormat rfc3339 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");

    public static String dateToRFC3339(final Date date) {
        return rfc3339.format(date);
    }

    public static Date rfc3339ToDate(final String date) {
        return Date.from(Instant.parse(date));
    }
}
