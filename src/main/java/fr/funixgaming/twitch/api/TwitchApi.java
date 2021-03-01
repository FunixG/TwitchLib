package fr.funixgaming.twitch.api;

public class TwitchApi {

    private final String clientID;
    private final String clientSecret;

    /**
     * Initialize TwitchApi Class to create an insance of connexion on Twitch services
     * Needs an ClientID and ClientSecret from TwitchAPI on your twitch api dashboard
     *
     * @param clientID String
     * @param clientSecret String
     */
    public TwitchApi(final String clientID, final String clientSecret) {
        this.clientID = clientID;
        this.clientSecret = clientSecret;
    }

    /**
     * Getter for clientID
     * @return clientID String
     */
    public String getClientID() {
        return clientID;
    }

    /**
     * Getter for clientSecret
     * @return clientSecret String
     */
    public String getClientSecret() {
        return clientSecret;
    }
}
