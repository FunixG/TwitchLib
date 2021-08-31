package fr.funixgaming.twitch.api.chatbot_irc.events;

import fr.funixgaming.twitch.api.chatbot_irc.TwitchBot;
import fr.funixgaming.twitch.api.chatbot_irc.entities.ChatMessage;
import fr.funixgaming.twitch.api.chatbot_irc.parsers.NoticeEventParser;
import lombok.Getter;

@Getter
public class NewSubscriptionGiftEvent extends TwitchEvent {

    private final ChatMessage message;
    private final Integer months;
    private final String receiverUsername;
    private final String receiverId;
    private final String channel;

    public NewSubscriptionGiftEvent(final ChatMessage message, final NoticeEventParser parser, final TwitchBot bot) {
        super(bot);

        this.message = message;
        this.months = parser.getMonths();
        this.receiverUsername = parser.getSubGiftReceiverUsername();
        this.receiverId = parser.getSubGiftReceiverId();
        this.channel = parser.getChannel();
    }
}
