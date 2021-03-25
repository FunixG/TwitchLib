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
    public void onChannelHost(HostChannelEvent event) {
    }

    @Override
    public void onClearUserMessages(ClearUserMessagesEvent event) {
        System.out.println("-ClearUserMessagesEvent-\nclearedUser: " + event.getClearedUser() +
                "\nchannel: " + event.getChannel());
    }

    @Override
    public void onMessageDeleted(DeleteMessageEvent event) {
        System.out.println("-DeleteMessageEvent-\nMessageDeleted: " + event.getMessageDeleted() +
                "\nchannel: " + event.getChannel() +
                "\nmessageUUID: " + event.getMessageDeletedUUID() +
                "\nuserDeleted: " + event.getUserDeletedMessage());

    }

    @Override
    public void onUserChat(UserChatEvent event) {
        System.out.println("-ChatMessageEvent-\nuserColor: " + event.getUser().getColor() +
                "\ndisplayName: " + event.getUser().getDisplayName() +
                "\nuserID: " + event.getUser().getUserId() +
                "\nisStreamer: " + event.getChatMember().getBadges().isStreamer() +
                "\nisMod: " + event.getChatMember().getBadges().isModerator() +
                "\nisSub: " + event.getChatMember().getBadges().isSubscriber() +
                "\nisVIP: " + event.getChatMember().getBadges().isVIP() +
                "\nmessageSendAt: " + event.getMessage().getDateMessageSend() +
                "\nroomName: " + event.getChatMember().getChannelName() +
                "\nroomID: " + event.getChatMember().getRoomID() +
                "\nmessageID: " + event.getMessage().getMessageID() +
                "\nmessage: " + event.getMessage().getMessage() +
                "\nemotesID: " + event.getMessage().getEmotes().getEmotesID() +
                "\nemotesNBR: " + event.getMessage().getEmotes().countEmotes());
    }

    @Override
    public void onRoomStateChange(RoomStateChangeEvent event) {
        System.out.println("-RoomStateChangeEvent-\nchannel: " + event.getChannel() +
                "\nisEmoteOnly: " + event.isEmoteOnly() +
                "\nisFollowOnly: " + event.isFollowersOnly() +
                "\nisR9k: " + event.isR9k() +
                "\nisSlowMode: " + event.isSlowMode() +
                "\nisSubsOnly: " + event.isSubsOnly());
    }
}
