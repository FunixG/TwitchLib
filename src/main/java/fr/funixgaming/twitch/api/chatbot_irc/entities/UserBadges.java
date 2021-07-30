package fr.funixgaming.twitch.api.chatbot_irc.entities;

import fr.funixgaming.twitch.api.chatbot_irc.TagParser;
import lombok.Getter;

@Getter
public class UserBadges {

    private boolean isStreamer = false;
    private boolean isModerator = false;
    private boolean isSubscriber = false;
    private boolean isVIP = false;
    private boolean isTwitchStaff = false;
    private boolean isTwitchAdmin = false;
    private boolean isTwitchPartner = false;
    private boolean isPrimeUser = false;
    private boolean isTurboUser = false;

    public UserBadges(final TagParser parser) {
        final String badgesString = parser.getTagMap().getOrDefault("badges", "");

        if (badgesString.isEmpty()) return;

        for (final String badgeInfo : badgesString.split(",")) {
            final String[] badgeData = badgeInfo.split("/");

            if (badgeData.length > 0) {
                switch (badgeData[0]) {
                    case "broadcaster":
                        this.isStreamer = true;
                        break;
                    case "moderator":
                        this.isModerator = true;
                        break;
                    case "subscriber":
                        this.isSubscriber = true;
                        break;
                    case "vip":
                        this.isVIP = true;
                        break;
                    case "premium":
                        this.isPrimeUser = true;
                        break;
                    case "staff":
                        this.isTwitchStaff = true;
                        break;
                    case "admin":
                        this.isTwitchAdmin = true;
                        break;
                    case "partner":
                        this.isTwitchPartner = true;
                        break;
                    case "turbo":
                        this.isTurboUser = true;
                        break;
                }
            }
        }
    }

}
