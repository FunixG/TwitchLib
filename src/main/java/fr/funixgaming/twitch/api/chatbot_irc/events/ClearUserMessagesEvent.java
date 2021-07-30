package fr.funixgaming.twitch.api.chatbot_irc.events;

import fr.funixgaming.twitch.api.chatbot_irc.parsers.TagParser;
import fr.funixgaming.twitch.api.chatbot_irc.TwitchBot;
import lombok.Getter;

@Getter
public class ClearUserMessagesEvent extends TwitchEvent {

    private final String channel;
    private final String userName;

    public ClearUserMessagesEvent(final TagParser parser, final TwitchBot bot) {
        super(bot);
        this.channel = parser.getChannel();
        this.userName = parser.getMessage();
    }
}
