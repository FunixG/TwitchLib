package fr.funixgaming.twitch.api.reference.entities.responses.channel;

import fr.funixgaming.twitch.api.reference.entities.ApiEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Channel extends ApiEntity {
    private final String broadcasterId;
    private final String broadcasterName;
    private final String broadcasterDisplayName;
    private final String broadcasterLanguage;
    private final String gameId;
    private final String gameName;
    private final String streamTitle;
    private final Integer streamDelay;

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Channel) {
            final Channel channel = (Channel) obj;
            return channel.getBroadcasterId().equals(this.broadcasterId);
        } else {
            return false;
        }
    }
}
