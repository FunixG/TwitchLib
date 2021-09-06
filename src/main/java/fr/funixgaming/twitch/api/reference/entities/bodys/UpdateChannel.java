package fr.funixgaming.twitch.api.reference.entities.bodys;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateChannel {
    private String gameId;
    private String broadcasterLanguage;
    private String title;
    private String delay;
}
