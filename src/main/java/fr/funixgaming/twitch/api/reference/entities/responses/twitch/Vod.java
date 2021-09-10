package fr.funixgaming.twitch.api.reference.entities.responses.twitch;

import fr.funixgaming.twitch.api.reference.entities.ApiEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Date;

@Getter
@AllArgsConstructor
public class Vod extends ApiEntity {
    private final String id;
    private final String streamId;
    private final String userId;
    private final String userName;
    private final String userDisplayName;
    private final String title;
    private final String description;
    private final Date createdAt;
    private final Date publishedAt;
    private final String url;
    private final String thumbnailUrl;
    private final Integer views;
    private final String language;
    private final String duration;

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Vod) {
            final Vod vod = (Vod) obj;
            return vod.getId().equals(id);
        } else {
            return false;
        }
    }
}
