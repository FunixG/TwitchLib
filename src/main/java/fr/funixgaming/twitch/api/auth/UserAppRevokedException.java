package fr.funixgaming.twitch.api.auth;

import fr.funixgaming.twitch.api.tools.HttpCalls;

public class UserAppRevokedException extends Exception {

    public UserAppRevokedException(final HttpCalls.HttpJSONResponse response) {
        super("The twitch user connected to the app has revoked access token.\n" +
                "Response code: " + response.getResponseCode() + "\n" +
                "Response body: " + response.getBody());
    }
}
