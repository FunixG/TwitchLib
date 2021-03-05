package fr.funixgaming.twitch.api.chatbotIRC;

import fr.funixgaming.twitch.api.chatbotIRC.events.*;

public interface TwitchEvents {

    /**
     * Triggered when the bot join a twitch chat
     * @param event
     */
    default void onJoinEvent(JoinChatEvent event) {};

    /**
     * Triggered when the bot leaves a twitch chat
     * @param event
     */
    default void onLeaveEvent(LeaveChatEvent event) {};

    /**
     * Triggered when a streamer is hosting a channel
     * It's also triggered when a streamer stop hosting
     * @param event
     */
    default void onChannelHost(HostChannelEvent event) {};

    /**
     * Triggered when a stream chat is fully cleared
     * @param event
     */
    default void onChatClear(ClearChatEvent event) {};

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

}
