package fr.funixgaming.twitch.api.reference.entities.responses.channel;

import fr.funixgaming.twitch.api.reference.entities.TwitchApiEntity;
import fr.funixgaming.twitch.api.reference.entities.responses.TwitchImage;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ChannelReward extends TwitchApiEntity {
    @Getter
    @AllArgsConstructor
    public static class MaxRewardUsage {
        private final Boolean isEnabled;
        private final Integer amount;
    }

    private final String broadcasterId;
    private final String broadcasterName;
    private final String broadcasterDisplayName;
    private final String id;
    private final String title;
    private final String prompt;
    private final Integer cost;
    private final TwitchImage image;
    private final TwitchImage defaultImage;
    private final String backgroundColor;
    private final Boolean isEnabled;
    private final Boolean isUserInputRequired;
    private final MaxRewardUsage maxUsagePerStream;
    private final MaxRewardUsage maxUsagePerStreamUser;
    private final MaxRewardUsage globalCoolDown;
    private final Boolean isUsable;
    private final Boolean isInStock;
    private final Integer usagesCurrentStream;

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ChannelReward) {
            final ChannelReward reward = (ChannelReward) obj;
            return reward.getId().equals(this.id);
        } else {
            return false;
        }
    }
}
