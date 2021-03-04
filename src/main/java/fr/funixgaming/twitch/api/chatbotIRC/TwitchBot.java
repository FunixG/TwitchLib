package fr.funixgaming.twitch.api.chatbotIRC;

import fr.funixgaming.twitch.api.chatbotIRC.events.HostChannelEvent;
import fr.funixgaming.twitch.api.chatbotIRC.events.JoinChatEvent;
import fr.funixgaming.twitch.api.chatbotIRC.events.LeaveChatEvent;

import java.util.HashSet;
import java.util.Set;

public class TwitchBot extends IRCSocketClient {

    private final Set<TwitchEvents> twitchEvents;

    /**
     * Class constructor to initialize TwitchIRC methods
     * After initialisation, the program connects to Twitch IRC service
     *
     * @param botUsername String Bot username where the app will connect to
     * @param oauthToken String oAuth token to log the Bot to twitch (example: oauth:qsdj5476hjgvsdqjkhfdslk)
     */
    public TwitchBot(final String botUsername, final String oauthToken) {
        super("irc.chat.twitch.tv", 6697, botUsername, oauthToken);
        super.start();
        this.twitchEvents = new HashSet<>();
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

    public void addEventListener(final TwitchEvents eventInstance) {
        this.twitchEvents.add(eventInstance);
    }

    @Override
    protected void onSocketMessage(final String message) {
        if (message.equals("PING :tmi.twitch.tv")) {
            super.sendMessage("PONG :tmi.twitch.tv");
        } else {
            for (final String twitchData : message.split("\r\n")) {
                final TagParser parser = new TagParser(twitchData);

                if (parser.getTwitchTag() == null)
                    continue;

                for (final TwitchEvents evtInstance : this.twitchEvents) {
                    switch (parser.getTwitchTag()) {
                        case CLEARCHAT:
                            break;
                        case CLEARMSG:
                            break;
                        case GLOBALUSERSTATE:
                            break;
                        case PRIVMSG:
                            break;
                        case ROOMSTATE:
                            break;
                        case USERNOTICE:
                            break;
                        case USERSTATE:
                            break;
                        case NOTICE:
                            break;
                        case JOIN:
                            evtInstance.onJoinEvent(new JoinChatEvent(parser.getChannel(), this));
                            break;
                        case PART:
                            evtInstance.onLeaveEvent(new LeaveChatEvent(parser.getChannel(), this));
                            break;
                        case HOSTTARGET:
                            evtInstance.onChannelHost(new HostChannelEvent(parser, this));
                            break;
                    }
                }
            }
        }
    }
}
