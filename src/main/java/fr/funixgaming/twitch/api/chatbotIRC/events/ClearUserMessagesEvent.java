package fr.funixgaming.twitch.api.chatbotIRC.events;

import fr.funixgaming.twitch.api.chatbotIRC.TagParser;
import fr.funixgaming.twitch.api.chatbotIRC.TwitchBot;

public class ClearUserMessagesEvent extends TwitchEvent {

    private final String channel;
    private final String user;

    public ClearUserMessagesEvent(final TagParser parser, final TwitchBot bot) {
        super(bot);
        this.channel = parser.getChannel().substring(1);
        this.user = parser.getMessage().substring(1);
    }

    public String getChannel() {
        return this.channel;
    }

    public String getClearedUser() {
        return this.user;
    }
}
