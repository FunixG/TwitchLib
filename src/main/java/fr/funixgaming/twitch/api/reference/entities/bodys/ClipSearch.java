package fr.funixgaming.twitch.api.reference.entities.bodys;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class ClipSearch {
    private Integer numberOfClips;
    private Date startedAtSearch;
    private Date endedAtSearch;
    private String clipAfterCursor;
    private String clipBeforeCursor;
}
