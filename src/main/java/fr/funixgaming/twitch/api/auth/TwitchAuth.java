package fr.funixgaming.twitch.api.auth;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import lombok.Getter;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.Instant;
import java.util.Calendar;
import java.util.Date;
import java.util.Set;

import static fr.funixgaming.twitch.api.TwitchURLS.DOMAIN_TWITCH_AUTH_API;
import static fr.funixgaming.twitch.api.tools.HttpCalls.*;

@Getter
public class TwitchAuth {

    private final static String PATH_OAUTH_TOKEN = "/oauth2/token";

    private final String clientId;
    private final String clientSecret;
    private final Set<String> scopes;
    private String accessToken;
    private Date expirationDate;

    /**
     * Constructor used to create a new accessToken to call Twitch api services
     *
     * @param clientId Twitch client app ID -> Get It from your console developer on https://dev.twitch.tv/console/apps
     * @param clientSecret Twitch client app Secret (Don't leak it) -> Get it from https://dev.twitch.tv/console/apps
     * @param scopes List of permissions that the token will have access
     * @throws IOException when the twitch server has a problem
     */
    public TwitchAuth(final String clientId, final String clientSecret, final Set<String> scopes) throws IOException {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.scopes = scopes;

        refresh();
    }

    /**
     * Method used to check if your accessToken is valid
     */
    public boolean isValid() {
        final Instant now = Instant.now();
        final Instant expiration = this.expirationDate.toInstant();

        return now.isBefore(expiration.minusSeconds(30));
    }

    /**
     * Method used to generate a new accessToken
     */
    public void refresh() throws IOException {
        try {
            final URI url = new URI("https", DOMAIN_TWITCH_AUTH_API, PATH_OAUTH_TOKEN,
                    "client_id=" + clientId +
                            "&client_secret=" + clientSecret +
                            "&grant_type=client_credentials" +
                            "&scope=" + String.join(" ", scopes),
                    null
            );

            final HttpJSONResponse response = performJSONRequest(url.toURL(), HttpType.POST, null, null);
            if (response.getResponseCode() == 200) {
                final JsonObject body = response.getBody().getAsJsonObject();

                this.accessToken = body.get("access_token").getAsString();
                this.expirationDate = addSecondsToNowDate(body.get("expires_in").getAsInt());
            } else {
                throw new IOException("Error while fetching token on Twitch. Error code : " + response.getResponseCode());
            }
        } catch (URISyntaxException err) {
            throw new IOException(err);
        }
    }

    /**
     * Method to create a json version of this class that give you the ability to store it in a file or a database
     * @param prettyPrint Boolean, If true the method will return a json with spaces and line breaks if not it will return a string with no spaces (hard to read)
     * @return String jsonData of this class (Encoded by Gson google https://github.com/google/gson)
     */
    public String toJson(final boolean prettyPrint) {
        final GsonBuilder gsonBuilder = new GsonBuilder();

        if (prettyPrint) {
            gsonBuilder.setPrettyPrinting();
        }
        return gsonBuilder.create().toJson(this, TwitchAuth.class);
    }

    /**
     * Static Method to create a class instance from json (get it by calling the toJson() method)
     *
     * - Do not forget to call the method isValid() to check if your token is usable, if its not call the other constructor to create new one
     * @param json JsonData got from the call of toJson
     * @return twitch api class instance
     */
    public static TwitchAuth fromJson(final String json) {
        return new Gson().fromJson(json, TwitchAuth.class);
    }

    private Date addSecondsToNowDate(final int seconds) {
        final Calendar calendar = Calendar.getInstance();

        calendar.setTime(new Date());
        calendar.add(Calendar.SECOND, seconds);
        return calendar.getTime();
    }

}
