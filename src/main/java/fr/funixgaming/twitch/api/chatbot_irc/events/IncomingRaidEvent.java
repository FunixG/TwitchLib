package fr.funixgaming.twitch.api.chatbot_irc.events;

import fr.funixgaming.twitch.api.chatbot_irc.TwitchBot;
import fr.funixgaming.twitch.api.chatbot_irc.parsers.NoticeEventParser;
import lombok.Getter;

@Getter
public class IncomingRaidEvent extends TwitchEvent {

    private final String channelRevceivingRaidId;
    private final String raiderChannelName;
    private final Integer viewerCount;

    public IncomingRaidEvent(final NoticeEventParser parser, final TwitchBot bot) {
        super(bot);
        this.channelRevceivingRaidId = parser.getRoomId();
        this.raiderChannelName = parser.getRaiderUsername();
        this.viewerCount = parser.getRaidViewerCount();
    }
}
