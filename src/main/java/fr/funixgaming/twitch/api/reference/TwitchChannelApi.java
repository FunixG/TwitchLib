package fr.funixgaming.twitch.api.reference;

import com.google.gson.JsonObject;
import fr.funixgaming.twitch.api.TwitchURLS;
import fr.funixgaming.twitch.api.auth.TwitchAuth;
import fr.funixgaming.twitch.api.reference.entities.bodys.UpdateChannel;
import fr.funixgaming.twitch.api.reference.entities.responses.Channel;
import fr.funixgaming.twitch.api.tools.HttpCalls;
import lombok.AllArgsConstructor;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import static fr.funixgaming.twitch.api.tools.HttpCalls.HttpType;
import static fr.funixgaming.twitch.api.tools.HttpCalls.HttpJSONResponse;

@AllArgsConstructor
public class TwitchChannelApi {

    private final static String PATH = TwitchURLS.TWITCH_API_PATH + "/channels";
    private final TwitchAuth twitchAuth;

    public Channel getChannelInformation(final String channelId) throws IOException {
        try {
            if (!twitchAuth.isValid()) {
                twitchAuth.refresh();
            }

            final URI url = new URI(
                    "https",
                    TwitchURLS.DOMAIN_TWITCH_API,
                    PATH,
                    "broadcaster_id=" + channelId,
                    null
            );

            final HttpJSONResponse response = HttpCalls.performJSONRequest(url.toURL(), HttpType.GET, null, twitchAuth);
            if (response.getResponseCode() == 200) {
                final JsonObject data = response.getBody().getAsJsonObject().get("data").getAsJsonArray().get(0).getAsJsonObject();
                return new Channel(
                        data.get("broadcaster_id").getAsString(),
                        data.get("broadcaster_login").getAsString(),
                        data.get("broadcaster_name").getAsString(),
                        data.get("broadcaster_language").getAsString(),
                        data.get("game_id").getAsString(),
                        data.get("game_name").getAsString(),
                        data.get("title").getAsString(),
                        data.get("delay").getAsInt()
                );
            } else {
                throw new IOException("An error occurred while fetching channelId " + channelId + ". Http error code : " + response.getResponseCode());
            }
        } catch (URISyntaxException err) {
            throw new IOException(err);
        }
    }

    public void updateChannelInformation(final String channelId, final UpdateChannel updateChannel) throws IOException {
        try {
            if (!twitchAuth.isValid()) {
                twitchAuth.refresh();
            }

            final URI url = new URI(
                    "https",
                    TwitchURLS.DOMAIN_TWITCH_API,
                    PATH,
                    "broadcaster_id=" + channelId,
                    null
            );

            final JsonObject body = new JsonObject();
            if (updateChannel.getGameId() != null) {
                body.addProperty("game_id", updateChannel.getGameId());
            }
            if (updateChannel.getBroadcasterLanguage() != null) {
                body.addProperty("broadcaster_language", updateChannel.getBroadcasterLanguage());
            }
            if (updateChannel.getTitle() != null) {
                body.addProperty("title", updateChannel.getTitle());
            }
            if (updateChannel.getDelay() != null) {
                body.addProperty("delay", updateChannel.getDelay());
            }

            final HttpJSONResponse response = HttpCalls.performJSONRequest(url.toURL(), HttpType.PATCH, body.toString(), twitchAuth);
            if (response.getResponseCode() != 204) {
                throw new IOException("An error occurred while updating channelId " + channelId + ". Http error code : " + response.getResponseCode());
            }
        } catch (URISyntaxException err) {
            throw new IOException(err);
        }
    }

}
