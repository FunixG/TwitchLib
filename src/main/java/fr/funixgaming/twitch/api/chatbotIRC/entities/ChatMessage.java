package fr.funixgaming.twitch.api.chatbotIRC.entities;

import fr.funixgaming.twitch.api.chatbotIRC.TagParser;

import java.util.Date;
import java.util.Map;
import java.util.Set;

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

    public String getMessageWithoutEmotes() {
        final Set<String> emotesIndexes = this.emotes.getEmotesIndexes();
        StringBuilder toRet = new StringBuilder(this.message);
        int lastRemoved = 0;

        System.out.println(emotesIndexes);
        for (final String emoteIndexes : emotesIndexes) {
            final String[] indexes = emoteIndexes.split("-");

            if (indexes.length == 2) {
                int a1 = Integer.parseInt(indexes[0]);
                int a2 = Integer.parseInt(indexes[1]);
                System.out.println("s: " + (a1 - lastRemoved) + " e: " + (a2 - lastRemoved) + " sLen: " + toRet.length());
                toRet.delete(a1 - lastRemoved, a2 - lastRemoved);
                //TODO résoudre le problème, l'algo mange le texte
                lastRemoved += a2 - a1;
            }
        }
        return toRet.toString();
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
