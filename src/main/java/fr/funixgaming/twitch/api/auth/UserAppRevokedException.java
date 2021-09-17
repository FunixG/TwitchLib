package fr.funixgaming.twitch.api.auth;

public class UserAppRevokedException extends Exception {

    public UserAppRevokedException() {
        super("The twitch user connected to the app has revoked access token");
    }
}
