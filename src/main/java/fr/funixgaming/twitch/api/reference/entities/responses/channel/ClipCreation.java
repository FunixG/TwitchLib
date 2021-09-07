package fr.funixgaming.twitch.api.reference.entities.responses.channel;

import fr.funixgaming.twitch.api.reference.entities.TwitchApiEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ClipCreation extends TwitchApiEntity {
    private final String id;
    private final String editUrl;

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ClipCreation) {
            final ClipCreation clipCreation = (ClipCreation) obj;
            return clipCreation.getId().equals(id);
        } else {
            return false;
        }
    }
}
