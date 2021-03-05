package fr.funixgaming.twitch.api.chatbotIRC.events;

import fr.funixgaming.twitch.api.chatbotIRC.TwitchBot;

public class ClearChatEvent extends TwitchEvent {

    private final String streamName;

    public ClearChatEvent(final String stream, final TwitchBot bot) {
        super(bot);
        this.streamName = stream.substring(1);
    }

    public String getStreamName() {
        return this.streamName;
    }
}
