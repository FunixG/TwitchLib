package fr.funixgaming.twitch.api.reference.entities.bodys;

import fr.funixgaming.twitch.api.reference.entities.TwitchApiEntity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateChannel extends TwitchApiEntity {
    private String gameId;
    private String broadcasterLanguage;
    private String title;
    private String delay;

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof UpdateChannel) {
            final UpdateChannel updateChannel = (UpdateChannel) obj;
            return updateChannel.getTitle().equals(title) &&
                    updateChannel.getDelay().equals(delay) &&
                    updateChannel.getBroadcasterLanguage().equals(broadcasterLanguage) &&
                    updateChannel.getGameId().equals(gameId);
        } else {
            return false;
        }
    }
}
