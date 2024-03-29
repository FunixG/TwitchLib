package fr.funixgaming.twitch.api.chatbot_irc;

import fr.funixgaming.twitch.api.chatbot_irc.entities.ChatMember;
import fr.funixgaming.twitch.api.chatbot_irc.entities.ChatMessage;
import fr.funixgaming.twitch.api.chatbot_irc.events.*;
import fr.funixgaming.twitch.api.chatbot_irc.parsers.NoticeEventParser;
import fr.funixgaming.twitch.api.chatbot_irc.parsers.TagParser;
import fr.funixgaming.twitch.api.exceptions.TwitchIRCException;
import lombok.Getter;

import java.io.IOException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class TwitchBot extends IRCSocketClient {

    private static final String URL_TWITCH_CHAT_IRC = "irc.chat.twitch.tv";
    private static final int IRC_CHAT_PORT_SSL = 6697;

    private final List<TwitchEvents> twitchEvents = new ArrayList<>();

    /**
     * Class constructor to initialize TwitchIRC methods
     * After initialisation, the program connects to Twitch IRC service
     *
     * @param botUsername String Bot username where the app will connect to
     * @param oAuthCode oAuthCode, obtain it from <a href="https://twitchapps.com/tmi/">Oauth generator</a>
     */
    public TwitchBot(final String botUsername, final String oAuthCode) throws TwitchIRCException {
        super(URL_TWITCH_CHAT_IRC, IRC_CHAT_PORT_SSL, botUsername, oAuthCode);

        final Instant startTime = Instant.now();
        while (!super.isConnected()) {
            final Instant now = Instant.now();

            if (now.isAfter(startTime.plus(1, ChronoUnit.MINUTES))) {
                throw new TwitchIRCException("TimeOut of Twwitch IRC login. Check your credentials or console for informations.");
            }
        }
    }

    /**
     * Used to join a Twitch chat, needed for events listening
     * @param channelsName Twitch channel name. Example FunixGaming
     */
    public void joinChannel(final String ...channelsName) {
        for (final String channel : channelsName) {
            this.joinChannel(channel);
        }
    }

    /**
     * Quit a twitch chat disabling events listening
     * @param channelsName Twitch channel name. Example FunixGaming
     */
    public void leaveChannel(final String ...channelsName) {
        for (final String channel : channelsName) {
            this.leaveChannel(channel);
        }
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
        if (message.startsWith("PING")) {
            final String[] split = message.split(" ");

            if (split.length == 2) {
                super.sendMessage("PONG " + split[1]);
            }
        } else if (message.startsWith(":tmi.twitch.tv RECONNECT")) {
            super.getLogger().log(Level.INFO, "RECONNECT Twitch state received, now reconnecting...");
            super.needRestart = false;
        } else {
            final TagParser parser = new TagParser(message);
            if (parser.getTwitchTag() == null) {
                return;
            }

            for (final TwitchEvents evtInstance : this.twitchEvents) {
                switch (parser.getTwitchTag()) {
                    case CLEARCHAT -> evtInstance.onClearUserMessages(new ClearUserMessagesEvent(parser, this));
                    case CLEARMSG -> evtInstance.onMessageDeleted(new DeleteMessageEvent(parser, this));
                    case PRIVMSG -> evtInstance.onUserChat(new UserChatEvent(parser, this));
                    case ROOMSTATE -> evtInstance.onRoomStateChange(new RoomStateChangeEvent(this, parser));
                    case USERNOTICE -> handleUserNoticeEvent(evtInstance, parser);
                    case HOSTTARGET -> evtInstance.onChannelHost(new HostChannelEvent(parser, this));
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
