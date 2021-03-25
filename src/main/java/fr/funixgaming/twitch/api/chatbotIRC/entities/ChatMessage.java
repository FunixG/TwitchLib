package fr.funixgaming.twitch.api.chatbotIRC.entities;

import java.util.Date;

public class ChatMessage extends ChatMember {

    private final String message;
    private final String messageID;
    private final long timestampSend;
    private final MessageEmotes emotes;

    public ChatMessage(final ChatMember chatMember,
                       final String message,
                       final long timestampSend,
                       final String messageID,
                       final MessageEmotes emotes) {
        super(chatMember.user, chatMember.roomID, chatMember.channelName, chatMember.badges);
        this.message = message;
        this.timestampSend = timestampSend;
        this.messageID = messageID;
        this.emotes = emotes;
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
}
