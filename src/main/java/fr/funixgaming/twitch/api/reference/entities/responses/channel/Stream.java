package fr.funixgaming.twitch.api.reference.entities.responses.channel;

import fr.funixgaming.twitch.api.reference.entities.ApiEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Date;

@Getter
@AllArgsConstructor
public class Stream extends ApiEntity {
    private final String id;
    private final String userId;
    private final String userName;
    private final String userDisplayName;
    private final String gameId;
    private final String gameName;
    private final String title;
    private final Integer viewers;
    private final Date startedAt;
    private final String language;
    private final String thumbnailUrl;
    private final Boolean isMature;

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Stream) {
            final Stream stream = (Stream) obj;
            return stream.getId().equals(id);
        } else {
            return false;
        }
    }
}
