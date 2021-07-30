package fr.funixgaming.twitch.api.chatbot_irc;

import fr.funixgaming.twitch.api.chatbot_irc.entities.*;
import fr.funixgaming.twitch.api.chatbot_irc.events.*;

import java.util.ArrayList;
import java.util.List;

public class TwitchBot extends IRCSocketClient {

    private static final String URL_TWITCH_CHAT_IRC = "irc.chat.twitch.tv";
    private static final int IRC_CHAT_PORT_SSL = 6697;

    private final List<TwitchEvents> twitchEvents = new ArrayList<>();

    /**
     * Class constructor to initialize TwitchIRC methods
     * After initialisation, the program connects to Twitch IRC service
     *
     * @param botUsername String Bot username where the app will connect to
     * @param oauthToken String oAuth token to log the Bot to twitch (example: oauth:qsdj5476hjgvsdqjkhfdslk)
     */
    public TwitchBot(final String botUsername, final String oauthToken) {
        super(URL_TWITCH_CHAT_IRC, IRC_CHAT_PORT_SSL, botUsername, oauthToken);
    }

    /**
     * Used to join a Twitch chat, needed for events listening
     * @param channelsName Twitch channel name. Example FunixGaming
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
     * Quit a twitch chat disabling events listening
     * @param channelsName Twitch channel name. Example FunixGaming
     */
    public void leaveChannel(final String ...channelsName) {
        final StringBuilder stringBuilder = new StringBuilder();

        for (final String channel : channelsName) {
            stringBuilder.append("PART #");
            stringBuilder.append(channel);
            stringBuilder.append('\n');
        }
        super.sendMessage(stringBuilder.toString());
    }

    /**
     * Used to send a message to a channel, no need to join the channel to send the message
     * @param channelName Twitch channel name. Example FunixGaming
     * @param message String message to send in the chat.
     */
    public void sendMessageToChannel(final String channelName, final String message) {
        super.sendMessage("PRIVMSG #" + channelName + " :" + message);
    }

    /**
     * Used to register an event class to send twitch IRC events
     * @param eventInstance Class who will receive events
     */
    public void addEventListener(final TwitchEvents eventInstance) {
        this.twitchEvents.add(eventInstance);
    }

    @Override
    protected void onSocketMessage(final String message) {
        if (message.equals("PING :tmi.twitch.tv\r\n")) {
            super.sendMessage("PONG :tmi.twitch.tv");
        } else {
            for (final String twitchData : message.split("\r\n")) {
                final TagParser parser = new TagParser(twitchData);

                if (parser.getTwitchTag() == null)
                    continue;

                for (final TwitchEvents evtInstance : this.twitchEvents) {
                    switch (parser.getTwitchTag()) {
                        case CLEARCHAT:
                            evtInstance.onClearUserMessages(new ClearUserMessagesEvent(parser, this));
                            break;
                        case CLEARMSG:
                            evtInstance.onMessageDeleted(new DeleteMessageEvent(parser, this));
                            break;
                        case PRIVMSG:
                            evtInstance.onUserChat(new UserChatEvent(parser, this));
                            break;
                        case ROOMSTATE:
                            evtInstance.onRoomStateChange(new RoomStateChangeEvent(this, parser));
                            break;
                        case USERNOTICE:
                            this.handleUserNoticeEvent(evtInstance, parser);
                            break;
                        case HOSTTARGET:
                            evtInstance.onChannelHost(new HostChannelEvent(parser, this));
                            break;
                    }
                }
            }
        }
    }

    private void handleUserNoticeEvent(final TwitchEvents evtInstance, final TagParser parser) {
        final MessageEmotes emotes = new MessageEmotes(parser);
        final User user = new User(parser);
        final ChatMember member = new ChatMember(parser);
        //TODO parse and create event for user events (raids, subs) https://dev.twitch.tv/docs/irc/tags#usernotice-twitch-tags
    }
}
