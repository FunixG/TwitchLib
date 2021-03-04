package fr.funixgaming.twitch.api.chatbotIRC.enums;

import java.util.Arrays;
import java.util.Set;

public enum TwitchTag {
    CLEARCHAT("ban-duration"),
    CLEARMSG("login", "message", "target-msg-id"),
    GLOBALUSERSTATE("badge-info", "badges", "color", "display-name", "emote-sets", "user-id", "user-type"),
    PRIVMSG("badge-info", "badges", "bits", "color", "display-name", "emotes", "id", "message", "mod", "room-id",
            "tmi-sent-ts", "user-id"),
    ROOMSTATE("emote-only", "followers-only", "r9k", "slow", "subs-only"),
    USERNOTICE("badge-info", "badges", "color", "display-name", "emotes", "id", "login", "message", "mod", "msg-id",
            "room-id", "system-msg", "tmi-sent-ts", "user-id", "msg-param-cumulative-months", "msg-param-displayName",
            "msg-param-login", "msg-param-months", "msg-param-promo-gift-total", "msg-param-promo-name",
            "msg-param-recipient-display-name", "msg-param-recipient-id", "msg-param-recipient-user-name",
            "msg-param-sender-login", "msg-param-sender-name", "msg-param-should-share-streak",
            "msg-param-streak-months", "msg-param-sub-plan", "msg-param-sub-plan-name", "msg-param-viewerCount",
            "msg-param-ritual-name", "msg-param-threshold", "msg-param-gift-months"),
    USERSTATE("badge-info", "badges", "color", "display-name", "emote-sets", "mod"),
    NOTICE("msg-id"),
    JOIN(),
    PART(),
    HOSTTARGET();

    private final Set<String> tags;

    TwitchTag(final String ...tags) {
        this.tags = Set.copyOf(Arrays.asList(tags));
    }

    public Set<String> getTags() {
        return tags;
    }
}
