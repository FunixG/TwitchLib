package fr.funixgaming.twitch.api.chatbotIRC.events;

import fr.funixgaming.twitch.api.chatbotIRC.TwitchBot;

public class RoomStateChangeEvent extends TwitchEvent {

    public enum State {
        EMOTE_ONLY,
        FOLLOWERS_ONLY,
        SUBSCRIBERS_ONLY,
        R9K,
        SLOW_MODE,
    }

    private final String channel;
    private final String stateInfo;
    private final State state;

    public RoomStateChangeEvent(final String channel, final TwitchBot bot, final State state, final String stateInfo) {
        super(bot);
        this.channel = channel;
        this.state = state;
        this.stateInfo = stateInfo;
    }

    public String getChannel() {
        return channel;
    }

    public State getState() {
        return state;
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
