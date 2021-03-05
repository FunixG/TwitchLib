package fr.funixgaming.twitch.api.chatbotIRC.events;

import fr.funixgaming.twitch.api.chatbotIRC.TwitchBot;

public class LeaveChatEvent extends TwitchEvent {

    private final String channel;

    public LeaveChatEvent(final String channel, final TwitchBot bot) {
        super(bot);
        this.channel = channel.substring(1);
    }

    @Override
    public TwitchBot getBot() {
        return null;
    }

    public String getChannel() {
        return this.channel;
    }
}
