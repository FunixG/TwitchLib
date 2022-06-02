package fr.funixgaming.twitch.api.auth;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import fr.funixgaming.twitch.api.exceptions.TwitchApiException;
import fr.funixgaming.twitch.api.exceptions.UserAppRevokedException;
import fr.funixgaming.twitch.api.reference.TwitchApi;
import fr.funixgaming.twitch.api.tools.HttpCalls;
import lombok.Getter;

import java.io.IOException;
import java.net.URI;
import java.time.Instant;
import java.util.Date;

import static fr.funixgaming.twitch.api.tools.HttpCalls.*;

/**
 * Class used to call twitch api services, only used for backend purposes, somes api routes will crash using this token
 */
@Getter
public class TwitchAuth {

    private final static String PATH_OAUTH_TOKEN = "/oauth2/token";
    private final static String PATH_OAUTH_TOKEN_VALIDATE = "/oauth2/validate";

    private final String oauthCode;
    private transient String clientId;
    private transient String clientSecret;
    private String accessToken;
    private String refreshToken;
    private Date expirationDate;
    private String userName;
    private String userId;

    /**
     * Constructor used to create a new accessToken to call Twitch api services
     * Used when you already have a accessToken, refreshToken and expiration date
     * https://dev.twitch.tv/docs/authentication
     *
     * @param clientId Twitch client app ID -> Get It from your console developer on https://dev.twitch.tv/console/apps
     * @param clientSecret Twitch client app Secret (Don't leak it) -> Get it from https://dev.twitch.tv/console/apps
     * @param oauthCode Get the code from this : https://dev.twitch.tv/docs/authentication/getting-tokens-oauth#oauth-authorization-code-flow
     * @param accessToken app access token
     * @param refreshToken token to refresh
     * @param expirationDate expiration date of token
     * @throws TwitchApiException when the twitch server has a problem
     */
    public TwitchAuth(final String clientId,
                      final String clientSecret,
                      final String oauthCode,
                      final String accessToken,
                      final String refreshToken,
                      final Date expirationDate) throws TwitchApiException {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.oauthCode = oauthCode;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.expirationDate = expirationDate;

        if (!isValid()) {
            refresh();
        }
        isUsable();
    }

    /**
     * Constructor used hen you don't have accessToken with a specified user (oauth)
     *
     * @param clientId Twitch client app ID -> Get It from your console developer on https://dev.twitch.tv/console/apps
     * @param clientSecret Twitch client app Secret (Don't leak it) -> Get it from https://dev.twitch.tv/console/apps
     * @param oauthCode Get the code from this : https://dev.twitch.tv/docs/authentication/getting-tokens-oauth#oauth-authorization-code-flow
     * @param redirectUrl The redirect url of your app on twitch api
     * @throws TwitchApiException hen an error occurs
     */
    public TwitchAuth(final String clientId,
                      final String clientSecret,
                      final String oauthCode,
                      final String redirectUrl) throws TwitchApiException {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.oauthCode = oauthCode;

        try {
            final HttpCalls.HttpJSONResponse response = HttpCalls.performFormRequest(
                    URI.create("https://" + TwitchApi.DOMAIN_TWITCH_AUTH_API + PATH_OAUTH_TOKEN),
                    HttpType.POST,
                    "client_id=" + this.clientId +
                            "&client_secret=" + this.clientSecret +
                            "&code=" + this.oauthCode +
                            "&grant_type=authorization_code" +
                            "&redirect_uri=" + redirectUrl,
                    null
            );

            if (response.getResponseCode().equals(200)) {
                final JsonObject data = response.getBody().getAsJsonObject();

                this.accessToken = data.get("access_token").getAsString();
                this.refreshToken = data.get("refresh_token").getAsString();
                this.expirationDate = Date.from(Instant.now().plusSeconds(data.get("expires_in").getAsInt()));
                isUsable();
            } else {
                throw new IOException(String.format("The server returned an error. %s %s", response.getResponseCode(), response.getBody()));
            }
        } catch (IOException e) {
            throw new TwitchApiException("An error occured when creating tokens.", e);
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
     * @throws TwitchApiException when an error occurs
     */
    public boolean isUsable() throws TwitchApiException {
        try {
            final URI uri = URI.create("https://" + TwitchApi.DOMAIN_TWITCH_AUTH_API + PATH_OAUTH_TOKEN_VALIDATE);
            final HttpJSONResponse response = performJSONRequest(uri, HttpType.GET, null, this);

            if (response.getResponseCode().equals(200)) {
                final JsonObject data = response.getBody().getAsJsonObject();
                this.userName = data.get("login").getAsString();
                this.userId = data.get("user_id").getAsString();

                return true;
            } else {
                throw new UserAppRevokedException(response);
            }
        } catch (IOException e) {
            throw new TwitchApiException("An error occurred while checking if the token is usable.", e);
        }
    }

    /**
     * Method used to generate a new accessToken
     */
    public void refresh() throws TwitchApiException {
        try {
            final URI url = URI.create("https://" + TwitchApi.DOMAIN_TWITCH_AUTH_API + PATH_OAUTH_TOKEN +
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
                throw new IOException(String.format("Error while fetching token on Twitch. Error code : %s Body %s", response.getResponseCode(), response.getBody()));
            }
        } catch (IOException e) {
            throw new TwitchApiException("An error occurred while refreshing tokens.", e);
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
    public static TwitchAuth fromJson(final String json, final String clientId, final String clientSecret) throws TwitchApiException {
        final TwitchAuth twitchAuth = new Gson().fromJson(json, TwitchAuth.class);

        twitchAuth.clientId = clientId;
        twitchAuth.clientSecret = clientSecret;
        if (!twitchAuth.isValid()) {
            twitchAuth.refresh();
        }
        twitchAuth.isUsable();
        return twitchAuth;
    }

    @Override
    public String toString() {
        return this.toJson(false);
    }
}
