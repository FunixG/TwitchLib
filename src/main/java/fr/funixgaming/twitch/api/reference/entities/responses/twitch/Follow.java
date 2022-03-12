package fr.funixgaming.twitch.api.reference.entities.responses.twitch;

import fr.funixgaming.twitch.api.reference.entities.ApiEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Date;

@Getter
@AllArgsConstructor
public class Follow extends ApiEntity {
    private final String fromId;
    private final String fromName;
    private final String fromDisplayName;
    private final String toId;
    private final String toName;
    private final String toDisplayName;
    private final Date followedAt;
    private final Integer totalFollowers;

    @Override
    public boolean equals(Object obj) {
        return false;
    }
}
