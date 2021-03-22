package fr.funixgaming.twitch.api;

import fr.funixgaming.twitch.api.chatbotIRC.TwitchBot;
import fr.funixgaming.twitch.api.chatbotIRC.TwitchEvents;
import fr.funixgaming.twitch.api.chatbotIRC.events.*;

public class Main {

    public static void main(String[] args) {
        final TwitchBot twitchBot = new TwitchBot(args[0], args[1]);
        twitchBot.addEventListener(new TestEvents());
        twitchBot.joinChannel("funixgaming");
        twitchBot.sendMessageToChannel("funixgaming", "test");
        while (twitchBot.isRunning());
    }

}

class TestEvents implements TwitchEvents {

    @Override
    public void onJoinEvent(JoinChatEvent event) {
        System.out.println("JOIN EVENT: " + event.getChannel());
    }

    @Override
    public void onLeaveEvent(LeaveChatEvent event) {
        System.out.println("LEAVE EVENT: " + event.getChannel());
    }

    @Override
    public void onChannelHost(HostChannelEvent event) {
    }

    @Override
    public void onChatClear(ClearChatEvent event) {

    }

    @Override
    public void onClearUserMessages(ClearUserMessagesEvent event) {

    }

    @Override
    public void onMessageDeleted(DeleteMessageEvent event) {

    }

    @Override
    public void onUserChat(UserChatEvent event) {
        System.out.println("-ChatMessageEvent-\nuserColor: " + event.getUser().getColor() +
                "\ndisplayName: " + event.getUser().getDisplayName() +
                "\nuserID: " + event.getUser().getUserId() +
                "\nisStreamer: " + event.getChatMember().isStreamer() +
                "\nisMod: " + event.getChatMember().isModerator() +
                "\nisSub: " + event.getChatMember().isSubscriber() +
                "\nisVIP: " + event.getChatMember().isVIP());
    }
}