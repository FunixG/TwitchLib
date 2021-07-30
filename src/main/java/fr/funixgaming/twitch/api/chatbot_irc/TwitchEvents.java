package fr.funixgaming.twitch.api.chatbot_irc;

import fr.funixgaming.twitch.api.chatbot_irc.events.*;

public interface TwitchEvents {

    /**
     * Triggered when a streamer is hosting a channel
     * It's also triggered when a streamer stop hosting
     * @param event
     */
    default void onChannelHost(HostChannelEvent event) {};

    /**
     * Triggered when all user messages are removed (performed when a user is banned)
     * @param event
     */
    default void onClearUserMessages(ClearUserMessagesEvent event) {};

    /**
     * Triggered when a single message is deleted of a user
     * @param event
     */
    default void onMessageDeleted(DeleteMessageEvent event) {};

    /**
     * Triggered when the bot app is successfully logged to Twitch
     * @param event
     */
    default void onUserChat(UserChatEvent event) {};

    /**
     * Triggered when a channel activate the submode, follow only mode,
     * slow mode, emote only mode or the r9k mode
     * @param event
     */
    default void onRoomStateChange(RoomStateChangeEvent event) {};

}
