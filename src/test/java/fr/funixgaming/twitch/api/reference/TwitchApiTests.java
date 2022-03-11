package fr.funixgaming.twitch.api.reference;

import fr.funixgaming.twitch.api.auth.TwitchAuth;
import fr.funixgaming.twitch.api.auth.TwitchAuthTestUtils;
import fr.funixgaming.twitch.api.exceptions.TwitchApiException;
import fr.funixgaming.twitch.api.reference.entities.bodys.UpdateChannel;
import fr.funixgaming.twitch.api.reference.entities.responses.channel.Channel;
import fr.funixgaming.twitch.api.reference.entities.responses.channel.ChannelEmote;
import fr.funixgaming.twitch.api.reference.entities.responses.channel.ChannelReward;
import fr.funixgaming.twitch.api.reference.entities.responses.channel.Stream;
import fr.funixgaming.twitch.api.reference.entities.responses.twitch.ClipCreation;
import fr.funixgaming.twitch.api.reference.entities.responses.twitch.User;
import org.junit.jupiter.api.Test;

import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class TwitchApiTests {

    private static final String STREAM_USERNAME_TEST = "electricallongboard";
    private static final String USER_USERNAME_TEST = "funixgaming";

    private final TwitchAuth auth;
    private final TwitchApi api;

    public TwitchApiTests() throws TwitchApiException {
        this.auth = TwitchAuthTestUtils.getAuth();
        this.api = new TwitchApi(this.auth);
    }

    @Test
    public void testGetChannelInfoSuccess() throws TwitchApiException {
        final Channel channel = api.getChannelInformation(auth.getUserId());

        assertNotNull(channel.getBroadcasterId());
        assertNotNull(channel.getBroadcasterName());
        assertNotNull(channel.getBroadcasterDisplayName());
        assertNotNull(channel.getBroadcasterLanguage());
        assertNotNull(channel.getGameId());
        assertNotNull(channel.getGameName());
        assertNotNull(channel.getStreamTitle());
        assertNotNull(channel.getStreamDelay());
    }

    @Test
    public void testUpdateChannelInfo() throws TwitchApiException {
        final UpdateChannel updateChannel = new UpdateChannel();
        updateChannel.setTitle("[API-TEST] New title " + UUID.randomUUID());
        api.updateChannelInformation(auth.getUserId(), updateChannel);

        final Channel channel = api.getChannelInformation(auth.getUserId());
        assertEquals(updateChannel.getTitle(), channel.getStreamTitle());
    }

    @Test
    public void testGettingStream() throws TwitchApiException {
        final Set<Stream> streams = api.getStreamsByUserNames(Set.of(STREAM_USERNAME_TEST));

        if (streams.isEmpty()) {
            fail("list empty streamer offline");
        } else {
            for (final Stream stream : streams) {
                assertNotNull(stream.getId());
                assertNotNull(stream.getUserId());
                assertNotNull(stream.getUserName());
                assertNotNull(stream.getUserDisplayName());
                assertNotNull(stream.getGameId());
                assertNotNull(stream.getGameName());
                assertNotNull(stream.getTitle());
                assertNotNull(stream.getViewers());
                assertNotNull(stream.getStartedAt());
                assertNotNull(stream.getLanguage());
                assertNotNull(stream.getThumbnailUrl());
                assertNotNull(stream.getIsMature());

                final Set<Stream> list = api.getStreamsById(Set.of(stream.getUserId()));

                if (list.isEmpty()) {
                    fail("id list empty");
                } else {
                    for (final Stream stream1 : list) {
                        assertNotNull(stream1.getId());
                        assertNotNull(stream1.getUserId());
                        assertNotNull(stream1.getUserName());
                        assertNotNull(stream1.getUserDisplayName());
                        assertNotNull(stream1.getGameId());
                        assertNotNull(stream1.getGameName());
                        assertNotNull(stream1.getTitle());
                        assertNotNull(stream1.getViewers());
                        assertNotNull(stream1.getStartedAt());
                        assertNotNull(stream1.getLanguage());
                        assertNotNull(stream1.getThumbnailUrl());
                        assertNotNull(stream1.getIsMature());
                    }
                }
            }
        }
    }

    @Test
    public void testGettingUser() throws TwitchApiException {
        final Set<User> users = api.getUsersByUserName(Set.of(USER_USERNAME_TEST));

        if (users.isEmpty()) {
            fail(USER_USERNAME_TEST + " does not exists on twitch. please change username");
        } else {
            for (final User user : users) {
                assertNotNull(user.getId());
                assertNotNull(user.getBroadcasterType());
                assertNotNull(user.getChannelDescription());
                assertNotNull(user.getCreatedAt());
                assertNotNull(user.getName());
                assertNotNull(user.getDisplayName());
                assertNotNull(user.getOfflineImageUrl());
                assertNotNull(user.getViews());
                assertNotNull(user.getProfileImageUrl());

                final Set<User> idList = api.getUsersById(Set.of(user.getId()));

                if (idList.isEmpty()) {
                    fail("list empty");
                } else {
                    for (final User id : idList) {
                        assertNotNull(id.getId());
                        assertNotNull(id.getBroadcasterType());
                        assertNotNull(id.getChannelDescription());
                        assertNotNull(id.getCreatedAt());
                        assertNotNull(id.getName());
                        assertNotNull(id.getDisplayName());
                        assertNotNull(id.getOfflineImageUrl());
                        assertNotNull(id.getViews());
                        assertNotNull(id.getProfileImageUrl());
                    }
                }
            }
        }
    }

    @Test
    public void testGetChannelRewards() throws TwitchApiException {
        final Set<User> users = api.getUsersByUserName(Set.of(auth.getUserName()));

        for (final User user : users) {
            final Set<ChannelReward> rewards = api.getChannelCustomRewards(user.getId());

            if (rewards.isEmpty()) {
                fail("the channel has no custom rewards " + user.getDisplayName());
            } else {
                for (final ChannelReward reward : rewards) {
                    assertNotNull(reward.getId());
                    assertNotNull(reward.getBroadcasterId());
                    assertNotNull(reward.getBroadcasterName());
                    assertNotNull(reward.getBroadcasterDisplayName());
                    assertNotNull(reward.getTitle());
                    assertNotNull(reward.getPrompt());
                    assertNotNull(reward.getDefaultImage());
                    assertNotNull(reward.getBackgroundColor());
                    assertNotNull(reward.getCost());
                    assertNotNull(reward.getGlobalCoolDown());
                    assertNotNull(reward.getIsEnabled());
                    assertNotNull(reward.getIsInStock());
                    assertNotNull(reward.getIsUserInputRequired());
                    assertNotNull(reward.getMaxUsagePerStream());
                    assertNotNull(reward.getMaxUsagePerStreamUser());
                    assertNotNull(reward.getUsagesCurrentStream());
                }
            }
        }
    }

    @Test
    public void testGetChannelEmotes() throws TwitchApiException {
        final Set<ChannelEmote> channelEmotes = api.getChannelEmotes(auth.getUserId());

        for (final ChannelEmote channelEmote : channelEmotes) {
            assertNotNull(channelEmote.getId());
            assertNotNull(channelEmote.getName());
            assertNotNull(channelEmote.getImages());
            assertNotNull(channelEmote.getEmoteType());

            if (channelEmote.getEmoteType().equals(ChannelEmote.EmoteType.SUB)) {
                assertNotNull(channelEmote.getSubTier());
            }
        }
    }

    @Test
    public void testClipCreation() throws TwitchApiException {
        final Set<Stream> streams = api.getStreamsByUserNames(Set.of("zerator"));

        for (final Stream stream : streams) {
            final ClipCreation clipCreation = api.createClip(stream.getUserId());

            assertNotNull(clipCreation.getId());
            assertNotNull(clipCreation.getEditUrl());
        }
    }

}
