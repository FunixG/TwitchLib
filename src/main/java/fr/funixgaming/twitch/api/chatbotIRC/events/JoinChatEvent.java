package fr.funixgaming.twitch.api.chatbotIRC.events;

import fr.funixgaming.twitch.api.chatbotIRC.TwitchBot;

public class JoinChatEvent extends TwitchEvent {

    private final String channel;

    public JoinChatEvent(final String channel, final TwitchBot bot) {
        super(bot);
        this.channel = channel.substring(1);
    }

    public String getChannel() {
        return channel;
    }
}
