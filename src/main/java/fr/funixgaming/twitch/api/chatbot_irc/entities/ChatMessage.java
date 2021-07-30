package fr.funixgaming.twitch.api.chatbot_irc.entities;

import fr.funixgaming.twitch.api.chatbot_irc.TagParser;
import lombok.Getter;

import java.time.Instant;
import java.util.Map;

@Getter
public class ChatMessage {

    private final String message;
    private final String messageID;
    private final Instant dateRecieved;
    private final MessageEmotes emotes;
    private final ChatMember owner;

    public ChatMessage(final TagParser parser, final ChatMember chatMember) {
        final Map<String, String> params = parser.getTagMap();

        this.owner = chatMember;
        this.message = parser.getMessage().trim().replaceAll("\\s+", " ");
        this.dateRecieved = Instant.ofEpochMilli(Long.parseLong(params.get("tmi-sent-ts")));
        this.messageID = params.get("id");
        this.emotes = new MessageEmotes(parser);
    }
}
