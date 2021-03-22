package fr.funixgaming.twitch.api.chatbotIRC;

import fr.funixgaming.twitch.api.chatbotIRC.events.*;

import java.util.HashSet;
import java.util.Set;

public class TwitchBot extends IRCSocketClient {

    private static final String URL_TWITCH_CHAT_IRC = "irc.chat.twitch.tv";
    private static final int IRC_CHAT_PORT_SSL = 6697;

    private final Set<TwitchEvents> twitchEvents;

    /**
     * Class constructor to initialize TwitchIRC methods
     * After initialisation, the program connects to Twitch IRC service
     *
     * @param botUsername String Bot username where the app will connect to
     * @param oauthToken String oAuth token to log the Bot to twitch (example: oauth:qsdj5476hjgvsdqjkhfdslk)
     */
    public TwitchBot(final String botUsername, final String oauthToken) {
        super(URL_TWITCH_CHAT_IRC, IRC_CHAT_PORT_SSL, botUsername, oauthToken);
        super.start();
        this.twitchEvents = new HashSet<>();
    }

    /**
     * Used to join a Twitch chat, needed for listening for events
     * @param channelsName
     */
    public void joinChannel(final String ...channelsName) {
        final StringBuilder stringBuilder = new StringBuilder();

        for (final String channel : channelsName) {
            stringBuilder.append("JOIN #");
            stringBuilder.append(channel);
            stringBuilder.append('\n');
        }
        super.sendMessage(stringBuilder.toString());
    }

    /**
     * Used to send a message to a channel, no need to join the channel to send the message
     * @param channelName
     * @param message
     */
    public void sendMessageToChannel(final String channelName, final String message) {
        super.sendMessage("PRIVMSG #" + channelName + " :" + message);
    }

    /**
     * Used to register an event class to send twitch IRC events
     * @param eventInstance
     */
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
                            if (parser.getMessage() == null)
                                evtInstance.onChatClear(new ClearChatEvent(parser.getChannel(), this));
                            else
                                evtInstance.onClearUserMessages(new ClearUserMessagesEvent(parser, this));
                            break;
                        case CLEARMSG:
                            evtInstance.onMessageDeleted(new DeleteMessageEvent(parser, this));
                            break;
                        case PRIVMSG:
                            evtInstance.onUserChat(new UserChatEvent(parser, this));
                            break;
                        case ROOMSTATE:
                            break;
                        case USERNOTICE:
                            break;
                        case USERSTATE:
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