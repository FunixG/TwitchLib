package fr.funixgaming.twitch.api.chatbotIRC;

public interface TwitchChatEvents {
    void onUserChat(final String channel, final String user, final String message);
}
