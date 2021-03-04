package fr.funixgaming.twitch.api.chatbotIRC;

import fr.funixgaming.twitch.api.chatbotIRC.events.HostChannelEvent;
import fr.funixgaming.twitch.api.chatbotIRC.events.JoinChatEvent;
import fr.funixgaming.twitch.api.chatbotIRC.events.LeaveChatEvent;

public interface TwitchEvents {

    void onJoinEvent(JoinChatEvent event);
    void onLeaveEvent(LeaveChatEvent event);
    void onChannelHost(HostChannelEvent event);

}
