package fr.funixgaming.twitch.api.chatbotIRC.events;

import fr.funixgaming.twitch.api.chatbotIRC.TagParser;
import fr.funixgaming.twitch.api.chatbotIRC.TwitchBot;

import java.util.Map;

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

    public String getChannel() {
        return channel;
    }

    public String getMessageDeleted() {
        return messageDeleted;
    }

    public String getUserDeletedMessage() {
        return userDeletedMessage;
    }

    public String getMessageDeletedUUID() {
        return messageDeletedUUID;
    }
}
