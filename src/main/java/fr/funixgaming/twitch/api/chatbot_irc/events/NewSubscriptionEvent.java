package fr.funixgaming.twitch.api.chatbot_irc.events;

import fr.funixgaming.twitch.api.chatbot_irc.TwitchBot;
import fr.funixgaming.twitch.api.chatbot_irc.entities.ChatMember;
import fr.funixgaming.twitch.api.chatbot_irc.entities.ChatMessage;
import fr.funixgaming.twitch.api.chatbot_irc.parsers.NoticeEventParser;
import lombok.Getter;

@Getter
public class NewSubscriptionEvent extends TwitchEvent {

    private final String channel;
    private final ChatMember subUser;
    private final ChatMessage chatMessage;
    private final NoticeEventParser.SubTier subTier;
    private final Integer months;
    private final boolean isResub;

    public NewSubscriptionEvent(final ChatMember chatMember,
                                final ChatMessage chatMessage,
                                final NoticeEventParser parser,
                                final TwitchBot bot) {
        super(bot);

        this.channel = parser.getChannel();
        this.subUser = chatMember;
        this.chatMessage = chatMessage;
        this.subTier = parser.getSubTier();
        this.months = parser.getMonths();
        this.isResub = parser.getNoticeType().equals(NoticeEventParser.NoticeType.RESUB);
    }
}
