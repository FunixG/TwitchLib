package fr.funixgaming.twitch.api.chatbotIRC;

import java.util.HashSet;
import java.util.Set;

public class TwitchChatBot extends IRCSocketClient {

    private final Set<TwitchChatEvents> twitchChatEvents;

    /**
     * Class constructor to initialize TwitchIRC methods
     * After initialisation, the program connects to Twitch IRC service
     *
     * @param botUsername String Bot username where the app will connect to
     * @param oauthToken String oAuth token to log the Bot to twitch (example: oauth:qsdj5476hjgvsdqjkhfdslk)
     */
    public TwitchChatBot(final String botUsername, final String oauthToken) {
        super("irc.chat.twitch.tv", 6697, botUsername, oauthToken);
        super.start();
        this.twitchChatEvents = new HashSet<>();
    }

    public void joinChannel(final String ...channelsName) {
        final StringBuilder stringBuilder = new StringBuilder();

        for (final String channel : channelsName) {
            stringBuilder.append("JOIN #");
            stringBuilder.append(channel);
            stringBuilder.append('\n');
        }
        super.sendMessage(stringBuilder.toString());
    }

    public void sendMessageToChannel(final String channelName, final String message) {
        super.sendMessage("PRIVMSG #" + channelName + " :" + message);
    }

    public void addEventListener(final TwitchChatEvents eventInstance) {
        this.twitchChatEvents.add(eventInstance);
    }

    @Override
    protected void onSocketMessage(final String message) {
        System.out.println(message);
        if (message.equals("PING :tmi.twitch.tv")) {
            super.sendMessage("PONG :tmi.twitch.tv");
        }
    }
}
