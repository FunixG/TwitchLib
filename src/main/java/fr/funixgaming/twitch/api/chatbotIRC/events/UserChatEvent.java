package fr.funixgaming.twitch.api.chatbotIRC.events;

import fr.funixgaming.twitch.api.chatbotIRC.TagParser;
import fr.funixgaming.twitch.api.chatbotIRC.TwitchBot;
import fr.funixgaming.twitch.api.chatbotIRC.enums.TwitchBadge;

import java.util.HashMap;
import java.util.Map;

public class UserChatEvent extends TwitchEvent {

    private final String color;
    private final String displayName;
    private final int bits;
    private final int userID;
    private final Map<String, String> badges;

    public UserChatEvent(final TagParser parser, final TwitchBot bot) {
        super(bot);
        final Map<String, String> params = parser.getTagMap();

        this.badges = new HashMap<>();
        this.color = params.get("color");
        this.bits = Integer.parseInt(params.getOrDefault("bits", "0"));
        this.displayName = params.get("display-name");
        this.userID = Integer.parseInt(params.get("user-id"));
        for (final String badgeInfo : params.get("badges").split(",")) {
            final String[] badgeData = badgeInfo.split("/");

            this.badges.put(badgeData[0], badgeData[1]);
        }
    }

    public String getColor() {
        return this.color;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public int getUserID() {
        return this.userID;
    }

    public int getBits() {
        return bits;
    }
}
