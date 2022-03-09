package fr.funixgaming.twitch.api.auth;

import fr.funixgaming.twitch.api.exceptions.TwitchApiException;

public class TwitchAuthTestUtils {

    public static TwitchAuth getAuth() throws TwitchApiException {
        final String clientId = System.getenv("CLIENT_ID");
        final String clientSecret = System.getenv("CLIENT_SECRET");
        final String oauthCode = System.getenv("OAUTH_CODE");
        final String redirectUrl = System.getenv("REDIRECT_URI");

        final TwitchAuth twitchAuth = new TwitchAuth(
                clientId,
                clientSecret,
                oauthCode,
                redirectUrl
        );

        System.out.println("Logged user: " + twitchAuth.getUserName() + " id: " + twitchAuth.getUserId());
        return twitchAuth;
    }

}
