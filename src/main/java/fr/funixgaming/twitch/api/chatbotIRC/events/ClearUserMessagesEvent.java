package fr.funixgaming.twitch.api.chatbotIRC.events;

import fr.funixgaming.twitch.api.chatbotIRC.TagParser;
import fr.funixgaming.twitch.api.chatbotIRC.TwitchBot;

public class ClearUserMessagesEvent extends TwitchEvent {

    private final String channel;
    private final String user;

    public ClearUserMessagesEvent(final TagParser parser, final TwitchBot bot) {
        super(bot);
        this.channel = parser.getChannel();
        this.user = parser.getMessage();
    }

    public String getChannel() {
        return this.channel;
    }

    public String getClearedUser() {
        return this.user;
    }
}
