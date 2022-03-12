package fr.funixgaming.twitch.api.chatbot_irc;

import fr.funixgaming.twitch.api.auth.TwitchAuth;
import fr.funixgaming.twitch.api.chatbot_irc.entities.*;
import fr.funixgaming.twitch.api.chatbot_irc.events.*;
import fr.funixgaming.twitch.api.chatbot_irc.parsers.NoticeEventParser;
import fr.funixgaming.twitch.api.chatbot_irc.parsers.TagParser;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class TwitchBot extends IRCSocketClient {

    private static final String URL_TWITCH_CHAT_IRC = "irc.chat.twitch.tv";
    private static final int IRC_CHAT_PORT_SSL = 6697;

    @Getter private final TwitchCommands twitchCommands;
    private final List<TwitchEvents> twitchEvents = new ArrayList<>();

    /**
     * Class constructor to initialize TwitchIRC methods
     * After initialisation, the program connects to Twitch IRC service
     *
     * @param botUsername String Bot username where the app will connect to
     * @param oauthToken String oAuth token to log the Bot to twitch (example: qsdj5476hjgvsdqjkhfdslk)
     *                   <br>Needs scopes chat:read chat:edit channel:moderate whispers:read whispers:edit channel_editor
     *                    <br>- TwitchScopes.CHAT_EDIT
     *                    <br>- TwitchScopes.CHAT_READ
     *                    <br>- TwitchScopes.CHANNEL_MODERATE
     *                    <br>- TwitchScopes.CHANNEL_EDITOR
     *                    <br>- TwitchScopes.WHISPER_READ
     *                    <br>- TwitchScopes.WHISPER_EDIT
     */
    public TwitchBot(final String botUsername, final String oauthToken) {
        super(URL_TWITCH_CHAT_IRC, IRC_CHAT_PORT_SSL, botUsername, oauthToken);
        this.twitchCommands = new TwitchCommands(this);
    }

    /**
     * Class constructor to initialize TwitchIRC methods
     * After initialisation, the program connects to Twitch IRC service
     *
     * @param botUsername String Bot username where the app will connect to
     * @param auth auth class
     *                   <br>Needs scopes chat:read chat:edit channel:moderate whispers:read whispers:edit channel_editor
     *                    <br>- TwitchScopes.CHAT_EDIT
     *                    <br>- TwitchScopes.CHAT_READ
     *                    <br>- TwitchScopes.CHANNEL_MODERATE
     *                    <br>- TwitchScopes.CHANNEL_EDITOR
     *                    <br>- TwitchScopes.WHISPER_READ
     *                    <br>- TwitchScopes.WHISPER_EDIT
     */
    public TwitchBot(final String botUsername, final TwitchAuth auth) {
        super(URL_TWITCH_CHAT_IRC, IRC_CHAT_PORT_SSL, botUsername, auth.getAccessToken());
        this.twitchCommands = new TwitchCommands(this);
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

    public void sendPrivateMessage(final String user, final String message) {
        super.sendMessage("PRIVMSG :/w " + user + ' ' + message);
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
        final ChatMember member = new ChatMember(parser);
        final ChatMessage message = new ChatMessage(parser, member);
        final NoticeEventParser noticeEventParser = new NoticeEventParser(parser);

        if (noticeEventParser.getNoticeType() == null) {
            return;
        } else if (noticeEventParser.getNoticeType().equals(NoticeEventParser.NoticeType.SUB) ||
        noticeEventParser.getNoticeType().equals(NoticeEventParser.NoticeType.RESUB)) {
            evtInstance.onNewSubscription(new NewSubscriptionEvent(member, message, noticeEventParser, this));
        } else if (noticeEventParser.getNoticeType().equals(NoticeEventParser.NoticeType.SUB_GIFT) ||
        noticeEventParser.getNoticeType().equals(NoticeEventParser.NoticeType.SUBGIFT_ANONYM)) {
            evtInstance.onNewSubscriptionGift(new NewSubscriptionGiftEvent(message, noticeEventParser, this));
        } else if (noticeEventParser.getNoticeType().equals(NoticeEventParser.NoticeType.RAID)) {
            evtInstance.onIncomingRaid(new IncomingRaidEvent(noticeEventParser, this));
        }
    }
}
