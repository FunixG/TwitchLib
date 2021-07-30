package fr.funixgaming.twitch.api.chatbot_irc.events;

import fr.funixgaming.twitch.api.chatbot_irc.TagParser;
import fr.funixgaming.twitch.api.chatbot_irc.TwitchBot;
import lombok.Getter;

import java.util.Map;

public class RoomStateChangeEvent extends TwitchEvent {

    public enum State {
        EMOTE_ONLY,
        FOLLOWERS_ONLY,
        SUBSCRIBERS_ONLY,
        R9K,
        SLOW_MODE,
    }

    @Getter private final String channel;
    @Getter private State state;
    private String stateInfo;

    public RoomStateChangeEvent(final TwitchBot bot, final TagParser parser) {
        super(bot);

        final Map<String, String> params = parser.getTagMap();
        final String emoteOnly = params.get("emote-only");
        final String followOnly = params.get("followers-only");
        final String r9k = params.get("r9k");
        final String slow = params.get("slow");
        final String subs = params.get("subs-only");

        if (params.size() == 1) {
            this.channel = parser.getChannel();

            if (emoteOnly != null) {
                this.state = State.EMOTE_ONLY;
                this.stateInfo = emoteOnly;
            } else if (followOnly != null) {
                this.state = State.FOLLOWERS_ONLY;
                this.stateInfo = followOnly;
            } else if (r9k != null) {
                this.state = State.R9K;
                this.stateInfo = r9k;
            } else if (slow != null) {
                this.state = State.SLOW_MODE;
                this.stateInfo = slow;
            } else if (subs != null) {
                this.state = State.SUBSCRIBERS_ONLY;
                this.stateInfo = subs;
            }
        } else {
            throw new IllegalArgumentException("Missing twitch parameters.");
        }
    }

    public boolean isEnabled() {
        switch (this.state) {
            case SUBSCRIBERS_ONLY:
            case R9K:
            case EMOTE_ONLY:
                return stateInfo.equals("1");
            case SLOW_MODE:
                return !stateInfo.equals("0");
            case FOLLOWERS_ONLY:
                return !stateInfo.equals("-1");
            default:
                return false;
        }
    }

    /**
     * Will return the amount of time for follow mode (minutes)
     * Or returns the slow mode timer (in seconds)
     * @return int
     */
    public int getData() {
        return Integer.parseInt(this.stateInfo);
    }
}
