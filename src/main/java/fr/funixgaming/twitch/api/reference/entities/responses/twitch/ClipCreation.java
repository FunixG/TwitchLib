package fr.funixgaming.twitch.api.reference.entities.responses.twitch;

import fr.funixgaming.twitch.api.reference.entities.ApiEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ClipCreation extends ApiEntity {
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
