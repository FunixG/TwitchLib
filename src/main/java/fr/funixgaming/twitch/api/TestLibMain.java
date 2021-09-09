package fr.funixgaming.twitch.api;

import fr.funixgaming.twitch.api.auth.TwitchAuth;
import fr.funixgaming.twitch.api.auth.TwitchScopes;
import fr.funixgaming.twitch.api.chatbot_irc.TwitchBot;
import fr.funixgaming.twitch.api.chatbot_irc.TwitchEvents;
import fr.funixgaming.twitch.api.chatbot_irc.events.*;
import fr.funixgaming.twitch.api.reference.TwitchApi;
import fr.funixgaming.twitch.api.reference.entities.bodys.ClipSearch;
import fr.funixgaming.twitch.api.reference.entities.responses.channel.Stream;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.time.Instant;
import java.util.Date;
import java.util.Set;

@Getter
@Setter
class TestLibMain {

    private String botUsername;
    private String botOauthToken;
    private String apiClientId;
    private String apiClientSecret;
    private TwitchAuth twitchAuth;

    private static class TestEvents implements TwitchEvents {

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

    private void startBot() {
        final String channel = "funixbot";

        final Thread botThread = new Thread(() -> {
            final TwitchBot twitchBot = new TwitchBot(this.botUsername, this.botOauthToken);
            twitchBot.addEventListener(new TestEvents());
            twitchBot.joinChannel(channel);

            twitchBot.sendMessageToChannel("funixbot", "test");

            while (twitchBot.isRunning());
        });
        botThread.start();
    }

    private void getAuth(boolean testPrintAuth) {
        try {
            final TwitchAuth twitchAuth = new TwitchAuth(this.apiClientId, this.apiClientSecret, Set.of(
                    TwitchScopes.CHANNEL_MANAGE_SCHEDULE,
                    TwitchScopes.ANALYTICS_READ_EXTENSIONS,
                    TwitchScopes.ANALYTICS_READ_GAMES,
                    TwitchScopes.BITS_READ,
                    TwitchScopes.CHANNEL_EDIT_COMMERCIAL,
                    TwitchScopes.CHANNEL_MANAGE_BROADCAST,
                    TwitchScopes.CHANNEL_MANAGE_EXTENSIONS,
                    TwitchScopes.CHANNEL_MANAGE_POLLS,
                    TwitchScopes.CHANNEL_MANAGE_PREDICTIONS,
                    TwitchScopes.CHANNEL_MANAGE_CHAT_POINTS_REWARDS,
                    TwitchScopes.CHANNEL_MANAGE_VIDEOS,
                    TwitchScopes.CHANNEL_READ_EDITORS,
                    TwitchScopes.CHANNEL_READ_HYPE_TRAIN,
                    TwitchScopes.CHANNEL_READ_POLLS,
                    TwitchScopes.CHANNEL_READ_PREDICTIONS,
                    TwitchScopes.CHANNEL_READ_CHAT_POINTS_REWARDS,
                    TwitchScopes.CHANNEL_READ_SUBS,
                    TwitchScopes.EDIT_CLIPS,
                    TwitchScopes.MODERATION_READ,
                    TwitchScopes.MODERATION_MANAGE_AUTOMOD,
                    TwitchScopes.USER_EDIT,
                    TwitchScopes.USER_EDIT_FOLLOWS,
                    TwitchScopes.USER_MANAGE_BLOCKED_USER,
                    TwitchScopes.USER_READ_BLOCKED_USERS,
                    TwitchScopes.USER_READ_BROADCAST,
                    TwitchScopes.USER_READ_EMAIL,
                    TwitchScopes.USER_READ_FOLLOWS,
                    TwitchScopes.USER_READ_SUBS,
                    TwitchScopes.WHISPER
            ));
            this.twitchAuth = twitchAuth;

            if (testPrintAuth) {
                System.out.println(twitchAuth.toJson(true));
                System.out.println("isValid: " + twitchAuth.isValid());

                System.out.println("TEST DESERIALIZE");
                final TwitchAuth des = TwitchAuth.fromJson(twitchAuth.toJson(true));
                System.out.println(des.toJson(true));
                System.out.println("isValid: " + des.isValid());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void testChannelApi(final String channelId) {
        try {
            final TwitchApi api = new TwitchApi(this.twitchAuth);

            System.out.println("CHANNEL INFO\n" + api.getChannelInformation(channelId));
            Thread.sleep(1000);
            /*final UpdateChannel updateChannel = new UpdateChannel();
            updateChannel.setTitle("Test api");
            api.updateChannelInformation(channelId, updateChannel);*/
            System.out.println("CHANNEL CHAT EMOTES\n" + api.getChannelEmotes(channelId));
            Thread.sleep(1000);
            System.out.println("CHANNEL CHAT CLIPS NO PARAMS\n" + api.getChannelClips(channelId, null));
            Thread.sleep(1000);
            final ClipSearch search = new ClipSearch();
            search.setNumberOfClips(5);
            search.setStartedAtSearch(Date.from(Instant.now().minusSeconds(864000))); //10 jours
            search.setEndedAtSearch(Date.from(Instant.now()));
            System.out.println("CHANNEL CHAT CLIPS PARAMS LIMIT CLIPS AND DATE\n" + api.getChannelClips(channelId, search));
            Thread.sleep(1000);
            System.out.println("GET MINECRAFT GAME BY NAME\n" + api.getGameByName("minecraft"));
            Thread.sleep(1000);
            System.out.println("GET MINECRAFT GAME BY ID\n" + api.getGameById("27471"));

            Thread.sleep(1000);
            System.out.println("GET FUNIXGAMING STREAM\n" + api.getStreamsByUserNames(Set.of("funixgaming")));
            Thread.sleep(1000);
            System.out.println("GET SOLARY STREAM\n" + api.getStreamsByUserNames(Set.of("solary")));
            Thread.sleep(1000);
            System.out.println("GET MULTIPLE STREAMS\n" + api.getStreamsByUserNames(Set.of("ponce", "domingo", "aypierre")));

            Thread.sleep(1000);
            System.out.println("GET USER FUNIXGAMING\n" + api.getUsersByUserName(Set.of("funixgaming")));
            Thread.sleep(1000);
            System.out.println("GET MULTIPLE USERS\n" + api.getUsersByUserName(Set.of("drakkades", "luxlechien", "jestair", "zerator", "funixbot")));

            /*Thread.sleep(1000);
            System.out.println("CHANNEL CHAT REWARDS\n" + api.getChannelCustomRewards(channelId));*/
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        final TestLibMain main = new TestLibMain();
        main.setBotUsername(args[0]);
        main.setBotOauthToken(args[1]);
        main.setApiClientId(args[2]);
        main.setApiClientSecret(args[3]);

        final String channelIdToTest = args[4];

        main.startBot();
        main.getAuth(false);
        main.testChannelApi(channelIdToTest);
    }

}

