package fr.funixgaming.twitch.api.chatbot_irc.events;

import fr.funixgaming.twitch.api.chatbot_irc.TwitchBot;
import lombok.Getter;

import java.time.Instant;

@Getter
public abstract class TwitchEvent {

    private final TwitchBot bot;
    private final Instant eventTriggered;

    public TwitchEvent(final TwitchBot bot) {
        this.bot = bot;
        this.eventTriggered = Instant.now();
    }
}
