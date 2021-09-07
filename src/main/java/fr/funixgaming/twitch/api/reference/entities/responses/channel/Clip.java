package fr.funixgaming.twitch.api.reference.entities.responses.channel;

import fr.funixgaming.twitch.api.reference.entities.ApiEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Date;

@Getter
@AllArgsConstructor
public class Clip extends ApiEntity {
    private final String id;
    private final String url;
    private final String embedUrl;
    private final String broadcasterId;
    private final String broadcasterDisplayName;
    private final String creatorId;
    private final String creatorName;
    private final String vodId;
    private final String gameId;
    private final String language;
    private final String title;
    private final Integer views;
    private final Date createdAt;
    private final String thumbnailUrl;
    private final Float duration;
    private final String paginationCursor;

    @Override
    public boolean equals(Object obj) {
        return false;
    }
}
