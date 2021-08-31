package fr.funixgaming.twitch.api.auth;

import com.google.gson.JsonObject;
import lombok.Getter;

import java.io.IOException;
import java.net.URL;
import java.time.Instant;
import java.util.Set;

import static fr.funixgaming.twitch.api.tools.HttpCalls.*;

@Getter
public class TwitchAuth {

    private final static String URL_OAUTH = "https://id.twitch.tv/oauth2/";

    private final String clientId;
    private final String accessToken;
    private final String refreshToken;
    private final Instant expirationDate;

    public TwitchAuth(final String clientId, final String clientSecret, final Set<String> scopes) throws IOException {
        this.clientId = clientId;

        final URL url = new URL(URL_OAUTH + "token" +
                "?client_id=" + clientId +
                "&client_secret=" + clientSecret +
                "&grant_type=client_credentials" +
                "&scope=" + String.join(" ", scopes)
        );

        final HttpResponse response = performRequest(url, HttpType.POST, null, null, true);
        if (response.getResponseCode() == 200) {
            final JsonObject body = response.getBody().getAsJsonObject();

            this.accessToken = body.get("access_token").getAsString();
            this.refreshToken = body.get("refresh_token").getAsString();
            this.expirationDate = Instant.now().plusSeconds(body.get("expires_in").getAsInt());
        } else {
            throw new IOException("Error while fetching token on Twitch. Error code : " + response.getResponseCode());
        }
    }

    public void refreshToken() {
        final URL url = new URL(URL_OAUTH + "token" +
                "?client_id=" + clientId +
                "&client_secret=" + clientSecret +
                "&grant_type=client_credentials" +
                "&scope=" + String.join(" ", scopes)
        );
    }

    public void revokeAccessToken() throws IOException {
        final URL url = new URL(URL_OAUTH + "revoke");
        final HttpResponse response = performRequest(url, HttpType.POST, "client_id=" + clientId + "&token=" + accessToken, null, false);

        if (response.getResponseCode() != 200) {
            throw new IOException("Bad request. The client_id is missing or invalid, or the token is missing or invalid. Error code : " + response.getResponseCode());
        }
    }

}
