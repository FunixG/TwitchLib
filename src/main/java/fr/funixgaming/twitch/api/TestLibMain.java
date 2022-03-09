/*package fr.funixgaming.twitch.api;

import fr.funixgaming.twitch.api.auth.TwitchAuth;
import fr.funixgaming.twitch.api.exceptions.UserAppRevokedException;
import fr.funixgaming.twitch.api.chatbot_irc.TwitchBot;
import fr.funixgaming.twitch.api.chatbot_irc.TwitchEvents;
import fr.funixgaming.twitch.api.chatbot_irc.events.*;
import fr.funixgaming.twitch.api.reference.TwitchApi;
import fr.funixgaming.twitch.api.reference.entities.bodys.ClipSearch;
import fr.funixgaming.twitch.api.reference.entities.responses.twitch.User;
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

    private void testChannelApi(final String channelId) {
        try {
            final TwitchApi api = new TwitchApi(this.twitchAuth);

            System.out.println("CHANNEL INFO\n" + api.getChannelInformation(channelId));
            Thread.sleep(1000);
            /*final UpdateChannel updateChannel = new UpdateChannel();
            updateChannel.setTitle("Test api");
            api.updateChannelInformation(channelId, updateChannel);
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

            Thread.sleep(1000);
            final Set<User> fetch = api.getUsersByUserName(Set.of("funixgaming", "zerator"));
            User zerator = null;
            User funixgaming = null;

            for (final User user : fetch) {
                if (user.getName().equalsIgnoreCase("funixgaming")) {
                    funixgaming = user;
                } else if (user.getName().equalsIgnoreCase("zerator")) {
                    zerator = user;
                }
            }
            System.out.println("IS FUNIXGAMING FOLLOWING ZERATOR ????\n" + api.isUserFollowing(funixgaming.getId(), zerator.getId()));
            Thread.sleep(1000);
            System.out.println("IS ZERATOR FOLLOWING FUNIXGAMING ????\n" + api.isUserFollowing(zerator.getId(), funixgaming.getId()));
            Thread.sleep(1000);
            System.out.println("FUNIXGAMING FOLLOW COUNT\n" + api.getUserLastFollowerAndFollowCount(funixgaming.getId()));
            Thread.sleep(1000);
            System.out.println("GET VOD STREAMS\n" + api.getStreamVodList(funixgaming.getId()));

            Thread.sleep(1000);
            System.out.println("GET LAST FUNIXGAMING SUB\n" + api.getStreamerLastSubAndCount(funixgaming.getId()));

            /*Thread.sleep(1000);
            System.out.println("TEST CLIP ON ZERATOR CHANNEL\n");
            final Set<Stream> zeratorFetch = api.getStreamsByUserNames(Set.of("zerator"));
            for (final Stream stream : zeratorFetch) {
                System.out.println(api.createClip(stream.getUserId()));
            }*/
            /*Thread.sleep(1000);
            System.out.println("CHANNEL CHAT REWARDS\n" + api.getChannelCustomRewards(channelId));
        } catch (IOException | InterruptedException | UserAppRevokedException e) {
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
        main.testChannelApi(channelIdToTest);
    }

}
*/
