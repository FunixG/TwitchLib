package fr.funixgaming.twitch.api;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;

public class TwitchResources {
    private final static SimpleDateFormat rfc3339 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
    public final static String DOMAIN_TWITCH_AUTH_API = "id.twitch.tv";
    public final static String DOMAIN_TWITCH_API = "api.twitch.tv";
    public final static String TWITCH_API_PATH = "/helix";

    public static String dateToRFC3339(final Date date) {
        return rfc3339.format(date).replaceAll("(\\d\\d)(\\d\\d)$", "$1:$2");
    }

    public static Date rfc3339ToDate(final String date) {
        return Date.from(Instant.parse(date));
    }
}
