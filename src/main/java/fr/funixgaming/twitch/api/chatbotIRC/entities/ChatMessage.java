package fr.funixgaming.twitch.api.chatbotIRC.entities;

import java.util.Date;

public class ChatMessage extends ChatMember {

    private final String message;
    private final String messageID;
    private final long timestampSend;

    public ChatMessage(final ChatMember chatMember,
                       final String message,
                       final long timestampSend,
                       final String messageID) {
        super(chatMember.user, chatMember.badges, chatMember.roomID, chatMember.channelName);
        this.message = message;
        this.timestampSend = timestampSend;
        this.messageID = messageID;
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
}
