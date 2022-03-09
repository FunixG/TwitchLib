package fr.funixgaming.twitch.api.exceptions;

public class TwitchApiException extends Exception {
    public TwitchApiException() {
        super();
    }

    public TwitchApiException(String message) {
        super(message);
    }

    public TwitchApiException(String message, Throwable cause) {
        super(message, cause);
    }

    public TwitchApiException(Throwable cause) {
        super(cause);
    }
}
