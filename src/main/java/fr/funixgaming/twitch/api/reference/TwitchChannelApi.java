package fr.funixgaming.twitch.api.reference;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import fr.funixgaming.twitch.api.TwitchURLS;
import fr.funixgaming.twitch.api.auth.TwitchAuth;
import fr.funixgaming.twitch.api.chatbot_irc.parsers.NoticeEventParser;
import fr.funixgaming.twitch.api.reference.entities.bodys.UpdateChannel;
import fr.funixgaming.twitch.api.reference.entities.responses.channel.Channel;
import fr.funixgaming.twitch.api.reference.entities.responses.channel.ChannelEmotes;
import fr.funixgaming.twitch.api.reference.entities.responses.channel.ChannelReward;
import fr.funixgaming.twitch.api.reference.entities.responses.TwitchImage;
import fr.funixgaming.twitch.api.tools.HttpCalls;
import lombok.AllArgsConstructor;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.Set;

import static fr.funixgaming.twitch.api.tools.HttpCalls.HttpType;
import static fr.funixgaming.twitch.api.tools.HttpCalls.HttpJSONResponse;

@AllArgsConstructor
public class TwitchChannelApi {

    private final static String PATH_CHANNEL = TwitchURLS.TWITCH_API_PATH + "/channels";
    private final static String PATH_CHANNEL_POINTS = TwitchURLS.TWITCH_API_PATH + "/channel_points/custom_rewards";
    private final static String PATH_CHANNEL_CHAT = TwitchURLS.TWITCH_API_PATH + "/chat/emotes";

    private final TwitchAuth twitchAuth;

    public Channel getChannelInformation(final String channelId) throws IOException {
        try {
            if (!twitchAuth.isValid()) {
                twitchAuth.refresh();
            }

            final URI url = new URI(
                    "https",
                    TwitchURLS.DOMAIN_TWITCH_API,
                    PATH_CHANNEL,
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
                    PATH_CHANNEL,
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

    public Set<ChannelReward> getChannelCustomRewards(final String channelId) throws IOException {
        try {
            if (!twitchAuth.isValid()) {
                twitchAuth.refresh();
            }

            final URI url = new URI(
                    "https",
                    TwitchURLS.DOMAIN_TWITCH_API,
                    PATH_CHANNEL_POINTS,
                    "broadcaster_id=" + channelId,
                    null
            );

            final HttpJSONResponse response = HttpCalls.performJSONRequest(url.toURL(), HttpType.GET, null, twitchAuth);
            if (response.getResponseCode() == 200) {
                final Set<ChannelReward> rewards = new HashSet<>();
                final JsonArray data = response.getBody().getAsJsonObject().get("data").getAsJsonArray();

                for (final JsonElement element : data) {
                    final JsonObject reward = element.getAsJsonObject();

                    final JsonObject image = reward.get("image").getAsJsonObject();
                    final JsonObject defaultImage = reward.get("default_image").getAsJsonObject();
                    final JsonObject maxUsageStream = reward.get("max_per_stream_setting").getAsJsonObject();
                    final JsonObject maxUsageStreamUser = reward.get("max_per_user_per_stream_setting").getAsJsonObject();
                    final JsonObject globalCoolDown = reward.get("global_cooldown_setting").getAsJsonObject();
                    final JsonElement usageStreamData = reward.get("redemptions_redeemed_current_stream");
                    final int usageStream;

                    if (usageStreamData.isJsonNull()) {
                        usageStream = 0;
                    } else {
                        usageStream = usageStreamData.getAsInt();
                    }

                    rewards.add(new ChannelReward(
                            reward.get("broadcaster_id").getAsString(),
                            reward.get("broadcaster_login").getAsString(),
                            reward.get("broadcaster_name").getAsString(),
                            reward.get("id").getAsString(),
                            reward.get("title").getAsString(),
                            reward.get("prompt").getAsString(),
                            reward.get("cost").getAsInt(),
                            new TwitchImage(
                                    image.get("url_1x").getAsString(),
                                    image.get("url_2x").getAsString(),
                                    image.get("url_4x").getAsString()
                            ),
                            new TwitchImage(
                                    defaultImage.get("url_1x").getAsString(),
                                    defaultImage.get("url_2x").getAsString(),
                                    defaultImage.get("url_4x").getAsString()
                            ),
                            reward.get("background_color").getAsString(),
                            reward.get("is_enabled").getAsBoolean(),
                            reward.get("is_user_input_required").getAsBoolean(),
                            new ChannelReward.MaxRewardUsage(
                                    maxUsageStream.get("is_enabled").getAsBoolean(),
                                    maxUsageStream.get("max_per_stream").getAsInt()
                            ),
                            new ChannelReward.MaxRewardUsage(
                                    maxUsageStreamUser.get("is_enabled").getAsBoolean(),
                                    maxUsageStreamUser.get("max_per_user_per_stream").getAsInt()
                            ),
                            new ChannelReward.MaxRewardUsage(
                                    globalCoolDown.get("is_enabled").getAsBoolean(),
                                    globalCoolDown.get("global_cooldown_seconds").getAsInt()
                            ),
                            reward.get("is_paused").getAsBoolean(),
                            reward.get("is_in_stock").getAsBoolean(),
                            usageStream
                    ));
                    return rewards;
                }
            } else {
                throw new IOException("An error occurred while fetching channel rewards. Http Error code : " + response.getResponseCode());
            }
        } catch (URISyntaxException err) {
            throw new IOException(err);
        }
        return null;
    }

    public Set<ChannelEmotes> getChannelEmotes(final String channelId) throws IOException {
        try {
            if (!twitchAuth.isValid()) {
                twitchAuth.refresh();
            }

            final URI url = new URI(
                    "https",
                    TwitchURLS.DOMAIN_TWITCH_API,
                    PATH_CHANNEL_CHAT,
                    "broadcaster_id=" + channelId,
                    null
            );

            final HttpJSONResponse response = HttpCalls.performJSONRequest(url.toURL(), HttpType.GET, null, twitchAuth);
            if (response.getResponseCode() == 200) {
                final Set<ChannelEmotes> emotes = new HashSet<>();
                final JsonArray emotesData = response.getBody().getAsJsonObject().get("data").getAsJsonArray();

                for (final JsonElement emoteData : emotesData) {
                    final JsonObject data = emoteData.getAsJsonObject();
                    final JsonObject image = data.get("images").getAsJsonObject();
                    final String subTierData = data.get("tier").getAsString();
                    NoticeEventParser.SubTier subTier = null;

                    if (!subTierData.isEmpty()) {
                        for (final NoticeEventParser.SubTier sub : NoticeEventParser.SubTier.values()) {
                            if (sub.getTwitchTag().equals(subTierData)) {
                                subTier = sub;
                                break;
                            }
                        }
                    }

                    emotes.add(new ChannelEmotes(
                            data.get("id").getAsString(),
                            data.get("name").getAsString(),
                            new TwitchImage(
                                    image.get("url_1x").getAsString(),
                                    image.get("url_2x").getAsString(),
                                    image.get("url_4x").getAsString()
                            ),
                            subTier,
                            ChannelEmotes.EmoteType.getByType(data.get("emote_type").getAsString())
                    ));
                }
                return emotes;
            } else {
                throw new IOException("An error occurred while fetching channel emotes. Http Error code : " + response.getResponseCode());
            }
        } catch (URISyntaxException e) {
            throw new IOException(e);
        }
    }

}
