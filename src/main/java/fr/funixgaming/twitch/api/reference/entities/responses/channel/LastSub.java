package fr.funixgaming.twitch.api.reference.entities.responses.channel;

import fr.funixgaming.twitch.api.chatbot_irc.parsers.NoticeEventParser;
import fr.funixgaming.twitch.api.reference.entities.ApiEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LastSub extends ApiEntity {
    private final String broadcasterId;
    private final String broadcasterName;
    private final String broadcasterDisplayName;
    private final String gifterId;
    private final String gifterName;
    private final String gifterDisplayName;
    private final Boolean isGift;
    private final NoticeEventParser.SubTier subTier;
    private final String userId;
    private final String userName;
    private final String userDisplayName;
    private final Integer totalBroadcasterSubs;

    @Override
    public boolean equals(Object obj) {
        return false;
    }
}
