package fr.funixgaming.twitch.api.reference;

import fr.funixgaming.twitch.api.auth.TwitchAuth;
import fr.funixgaming.twitch.api.auth.TwitchAuthTestUtils;
import fr.funixgaming.twitch.api.exceptions.TwitchApiException;
import fr.funixgaming.twitch.api.reference.entities.responses.channel.Channel;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class TwitchApiTests {

    @Test
    public void testGetChannelInfoSuccess() throws TwitchApiException {
        final TwitchAuth twitchAuth = TwitchAuthTestUtils.getAuth();
        final Channel channel = new TwitchApi(twitchAuth).getChannelInformation(twitchAuth.getUserId());

        assertNotNull(channel.getBroadcasterId());
        assertNotNull(channel.getBroadcasterName());
        assertNotNull(channel.getBroadcasterDisplayName());
        assertNotNull(channel.getBroadcasterLanguage());
        assertNotNull(channel.getGameId());
        assertNotNull(channel.getGameName());
        assertNotNull(channel.getStreamTitle());
        assertNotNull(channel.getStreamDelay());
    }

}
