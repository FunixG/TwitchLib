package fr.funixgaming.twitch.api.chatbotIRC.events;

import fr.funixgaming.twitch.api.chatbotIRC.TagParser;
import fr.funixgaming.twitch.api.chatbotIRC.TwitchBot;

public class HostChannelEvent extends TwitchEvent {

    private final String channelHosted;
    private final String hostingChannel;
    private final int viewers;

    //TODO: Implement channel hosted parser
    public HostChannelEvent(final TagParser tagParser, final TwitchBot bot) {
        super(bot);
        this.hostingChannel = tagParser.getChannel();
        this.channelHosted = null;
        this.viewers = 0;
    }
}
