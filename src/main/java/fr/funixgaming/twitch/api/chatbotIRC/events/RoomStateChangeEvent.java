package fr.funixgaming.twitch.api.chatbotIRC.events;

import fr.funixgaming.twitch.api.chatbotIRC.TagParser;
import fr.funixgaming.twitch.api.chatbotIRC.TwitchBot;

import java.util.Map;

public class RoomStateChangeEvent extends TwitchEvent {

    private final String channel;

    private boolean emoteOnly = false;
    private boolean followersOnly = false;
    private boolean r9k = false;
    private boolean slowMode = false;
    private boolean subsOnly = false;

    public RoomStateChangeEvent(final TagParser parser, final TwitchBot bot) {
        super(bot);
        final Map<String, String> params = parser.getTagMap();

        this.channel = parser.getChannel();
        if (params.get("emote-only") != null && params.get("emote-only").equals("1"))
            this.emoteOnly = true;
        if (params.get("followers-only") != null && !params.get("followers-only").equals("-1"))
            this.followersOnly = true;
        if (params.get("r9k") != null && params.get("r9k").equals("1"))
            this.r9k = true;
        if (params.get("slow") != null && !params.get("slow").equals("0"))
            this.slowMode = true;
        if (params.get("subs-only") != null && params.get("subs-only").equals("1"))
            this.subsOnly = true;
    }

    public boolean isEmoteOnly() {
        return emoteOnly;
    }

    public boolean isFollowersOnly() {
        return followersOnly;
    }

    public boolean isR9k() {
        return r9k;
    }

    public boolean isSlowMode() {
        return slowMode;
    }

    public boolean isSubsOnly() {
        return subsOnly;
    }

    public String getChannel() {
        return channel;
    }
}
