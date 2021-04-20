package fr.funixgaming.twitch.api.chatbotIRC.entities;

import fr.funixgaming.twitch.api.chatbotIRC.TagParser;

import java.util.Date;
import java.util.Map;

public class ChatMessage {

    private final String message;
    private final String messageID;
    private final long timestampSend;
    private final MessageEmotes emotes;
    private final ChatMember owner;

    public ChatMessage(final TagParser parser, final ChatMember chatMember) {
        final Map<String, String> params = parser.getTagMap();

        this.owner = chatMember;
        this.message = parser.getMessage().trim().replaceAll("\\s+", " ");
        this.timestampSend = Long.parseLong(params.get("tmi-sent-ts"));
        this.messageID = params.get("id");
        this.emotes = new MessageEmotes(parser);
    }

    public String getMessage() {
        return message;
    }

    public Date getDateMessageSend() {
        return new Date(this.timestampSend);
    }

    public String getMessageID() {
        return messageID;
    }

    public MessageEmotes getEmotes() {
        return emotes;
    }

    public ChatMember getOwner() {
        return owner;
    }
}
