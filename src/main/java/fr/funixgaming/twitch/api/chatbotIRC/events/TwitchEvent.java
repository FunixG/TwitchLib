package fr.funixgaming.twitch.api.chatbotIRC.events;

import fr.funixgaming.twitch.api.chatbotIRC.TwitchBot;

import java.util.Date;

public abstract class TwitchEvent {

    private final TwitchBot bot;
    private final Date eventTriggered;

    public TwitchEvent(final TwitchBot bot) {
        this.bot = bot;
        this.eventTriggered = new Date();
    }

    public TwitchBot getBot() {
        return this.bot;
    }

    public Date getDate() {
        return eventTriggered;
    }
}
