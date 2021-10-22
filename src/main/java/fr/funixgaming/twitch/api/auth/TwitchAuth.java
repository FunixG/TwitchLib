package fr.funixgaming.twitch.api.auth;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import lombok.Getter;

import java.io.IOException;
import java.net.URI;
import java.time.Instant;
import java.util.Date;

import static fr.funixgaming.twitch.api.TwitchResources.DOMAIN_TWITCH_AUTH_API;
import static fr.funixgaming.twitch.api.tools.HttpCalls.*;

/**
 * Class used to call twitch api services, only used for backend purposes, somes api routes will crash using this token
 */
@Getter
public class TwitchAuth {

    private final static String PATH_OAUTH_TOKEN = "/oauth2/token";
    private final static String PATH_OAUTH_TOKEN_VALIDATE = "/oauth2/validate";

    private final String clientId;
    private final String clientSecret;
    private final String oauthCode;
    private String accessToken;
    private String refreshToken;
    private Date expirationDate;
    private String userName;
    private String userId;

    /**
     * Constructor used to create a new accessToken to call Twitch api services
     * https://dev.twitch.tv/docs/authentication
     *
     * @param clientId Twitch client app ID -> Get It from your console developer on https://dev.twitch.tv/console/apps
     * @param clientSecret Twitch client app Secret (Don't leak it) -> Get it from https://dev.twitch.tv/console/apps
     * @param oauthCode Get the code from this : https://dev.twitch.tv/docs/authentication/getting-tokens-oauth#oauth-authorization-code-flow
     * @param accessToken app access token
     * @param refreshToken token to refresh
     * @param expirationDate expiration date of token
     * @throws IOException when the twitch server has a problem
     */
    public TwitchAuth(final String clientId,
                      final String clientSecret,
                      final String oauthCode,
                      final String accessToken,
                      final String refreshToken,
                      final Date expirationDate) throws IOException, UserAppRevokedException {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.oauthCode = oauthCode;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.expirationDate = expirationDate;

        if (isUsable() && !isValid()) {
            refresh();
        }
    }

    /**
     * Method used to check if your accessToken is not expired
     */
    public boolean isValid() {
        final Instant now = Instant.now();
        final Instant expiration = this.expirationDate.toInstant();

        return now.isBefore(expiration.minusSeconds(30));
    }

    /**
     * Check if the user connected with twitch api has revoked or not the access
     *
     * @return bool to know if the user has revoked the app token
     * @throws IOException when an error
     */
    public boolean isUsable() throws IOException, UserAppRevokedException {
        final URI uri = URI.create("https://" + DOMAIN_TWITCH_AUTH_API + PATH_OAUTH_TOKEN_VALIDATE);
        final HttpJSONResponse response = performJSONRequest(uri, HttpType.GET, null, this);

        if (response.getResponseCode().equals(200)) {
            final JsonObject data = response.getBody().getAsJsonObject();
            this.userName = data.get("login").getAsString();
            this.userId = data.get("user_id").getAsString();

            return true;
        } else {
            throw new UserAppRevokedException();
        }
    }

    /**
     * Method used to generate a new accessToken
     */
    public void refresh() throws IOException {
        final URI url = URI.create("https://" + DOMAIN_TWITCH_AUTH_API + PATH_OAUTH_TOKEN +
                "?client_id=" + clientId +
                "&client_secret=" + clientSecret +
                "&refresh_token=" + refreshToken +
                "&grant_type=refresh_token"
        );

        final HttpJSONResponse response = performJSONRequest(url, HttpType.POST, null, null);
        if (response.getResponseCode() == 200) {
            final JsonObject body = response.getBody().getAsJsonObject();

            this.accessToken = body.get("access_token").getAsString();
            this.refreshToken = body.get("refresh_token").getAsString();
            this.expirationDate = Date.from(Instant.now().plusSeconds(body.get("expires_in").getAsInt()));
        } else {
            throw new IOException("Error while fetching token on Twitch. Error code : " + response.getResponseCode());
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

}
