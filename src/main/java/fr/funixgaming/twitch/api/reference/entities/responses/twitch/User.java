package fr.funixgaming.twitch.api.reference.entities.responses.twitch;

import fr.funixgaming.twitch.api.reference.entities.ApiEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Date;

@Getter
@AllArgsConstructor
public class User extends ApiEntity {

    public enum BroadcasterType {
        NORMAL,
        AFFILIATE,
        PARTNER //GG boi you did it <3
    }

    private final BroadcasterType broadcasterType;
    private final String channelDescription;
    private final String displayName;
    private final String id;
    private final String name;
    private final String offlineImageUrl;
    private final String profileImageUrl;
    private final Integer views;
    private final Date createdAt;

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof User) {
            final User user = (User) obj;
            return user.getId().equals(id);
        } else {
            return false;
        }
    }
}
