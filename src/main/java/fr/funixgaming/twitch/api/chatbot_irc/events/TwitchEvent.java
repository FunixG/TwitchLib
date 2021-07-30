package fr.funixgaming.twitch.api.chatbot_irc.events;

import fr.funixgaming.twitch.api.chatbot_irc.TwitchBot;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Instant;

@Getter
@AllArgsConstructor
public abstract class TwitchEvent {

    private final TwitchBot bot;
    private final Instant eventTriggered;

    public TwitchEvent(final TwitchBot bot) {
        this.bot = bot;
        this.eventTriggered = Instant.now();
    }
}
