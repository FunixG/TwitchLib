package fr.funixgaming.twitch.api.reference.entities.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Channel {
    private final String broadcasterId;
    private final String broadcasterName;
    private final String broadcasterDisplayName;
    private final String broadcasterLanguage;
    private final String gameId;
    private final String gameName;
    private final String streamTitle;
    private final Integer streamDelay;
}
