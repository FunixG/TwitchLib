package fr.funixgaming.twitch.api.chatbot_irc.events;

import fr.funixgaming.twitch.api.chatbot_irc.TagParser;
import fr.funixgaming.twitch.api.chatbot_irc.TwitchBot;
import lombok.Getter;

import java.util.Map;

@Getter
public class DeleteMessageEvent extends TwitchEvent {

    private final String channel;
    private final String messageDeleted;
    private final String userDeletedMessage;
    private final String messageDeletedUUID;

    public DeleteMessageEvent(final TagParser parser, final TwitchBot bot) {
        super(bot);
        final Map<String, String> params = parser.getTagMap();

        this.channel = parser.getChannel();
        this.messageDeleted = parser.getMessage();
        this.userDeletedMessage = params.get("login");
        this.messageDeletedUUID = params.get("target-msg-id");
    }
}
