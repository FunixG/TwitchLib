package fr.funixgaming.twitch.api.chatbot_irc;

import fr.funixgaming.twitch.api.chatbot_irc.events.*;

public class TestBotEvents implements TwitchEvents {
    @Override
    public void onChannelHost(HostChannelEvent event) {
    }

    @Override
    public void onClearUserMessages(ClearUserMessagesEvent event) {
        System.out.println("-ClearUserMessagesEvent-\nclearedUser: " + event.getUserName() +
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
        System.out.println("-ChatMessageEvent-\nuserColor: " + event.getChatMember().getColor() +
                "\ndisplayName: " + event.getChatMember().getDisplayName() +
                "\nloginName: " + event.getChatMember().getLoginName() +
                "\nuserID: " + event.getChatMember().getUserId() +
                "\nisStreamer: " + event.getChatMember().getBadges().isStreamer() +
                "\nisMod: " + event.getChatMember().getBadges().isModerator() +
                "\nisSub: " + event.getChatMember().getBadges().isSubscriber() +
                "\nisVIP: " + event.getChatMember().getBadges().isVIP() +
                "\nmessageSendAt: " + event.getChatMessage().getDateRecieved() +
                "\nroomName: " + event.getChatMember().getChannelName() +
                "\nroomID: " + event.getChatMember().getRoomID() +
                "\nmessageID: " + event.getChatMessage().getMessageID() +
                "\nmessage: " + event.getChatMessage().getMessage() +
                "\nemotesID: " + event.getChatMessage().getEmotes().getEmotesID() +
                "\nemotesNBR: " + event.getChatMessage().getEmotes().getEmotesNumber() +
                "\ntestGetOwnerOnMessage: " + event.getChatMessage().getOwner().getLoginName());
    }

    @Override
    public void onRoomStateChange(RoomStateChangeEvent event) {
        System.out.println("-RoomStateChangeEvent-\nchannel: " + event.getChannel() +
                "\nstate: " + event.getState() +
                "\nisEnabled: " + event.isEnabled() +
                "\ngetData: " + event.getData());
    }

    @Override
    public void onNewSubscription(NewSubscriptionEvent event) {
        System.out.println("--NEW SUB--\nchannel: " + event.getChannel() +
                "\nMonths: " + event.getMonths() +
                "\nSubTier: " + event.getSubTier() +
                "\nMessageSub: " + event.getChatMessage().getMessage() +
                "\nDisplayName: " + event.getSubUser().getDisplayName());
    }

    @Override
    public void onNewSubscriptionGift(NewSubscriptionGiftEvent event) {
        System.out.println("--SUB GIFT--\nchannel: " + event.getChannel() +
                "\nMessage: " + event.getMessage().getMessage() +
                "\nMonths: " + event.getMonths() +
                "\nReceiverUsername: " + event.getReceiverUsername());
    }

    @Override
    public void onIncomingRaid(IncomingRaidEvent event) {
        System.out.println("-- RAID INCOMING --\nchannel: " + event.getChannelNameReceivingRaid() +
                "\nViewers: " + event.getViewerCount() +
                "\nRaiderName: " + event.getRaiderChannelName());
    }
}
