package fr.funixgaming.twitch.api.chatbotIRC.enums;

public enum TwitchBadge {
    VIP("vip"),
    STREAMER("broadcaster");

    private final String badgeName;

    TwitchBadge(final String name) {
        this.badgeName = name;
    }

    public String getBadgeName() {
        return badgeName;
    }
}
