package fr.funixgaming.twitch.api.reference.entities.responses.twitch;

import fr.funixgaming.twitch.api.reference.entities.ApiEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Game extends ApiEntity {
    private final String id;
    private final String name;
    private final String boxArtUrl;

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ApiEntity) {
            final Game game = (Game) obj;
            return game.getId().equals(id);
        } else {
            return false;
        }
    }
}
