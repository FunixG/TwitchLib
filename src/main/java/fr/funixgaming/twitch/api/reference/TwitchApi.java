package fr.funixgaming.twitch.api.reference;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import fr.funixgaming.twitch.api.TwitchResources;
import fr.funixgaming.twitch.api.auth.TwitchAuth;
import fr.funixgaming.twitch.api.chatbot_irc.parsers.NoticeEventParser;
import fr.funixgaming.twitch.api.reference.entities.bodys.ClipSearch;
import fr.funixgaming.twitch.api.reference.entities.bodys.UpdateChannel;
import fr.funixgaming.twitch.api.reference.entities.responses.channel.*;
import fr.funixgaming.twitch.api.reference.entities.responses.TwitchImage;
import fr.funixgaming.twitch.api.reference.entities.responses.twitch.Clip;
import fr.funixgaming.twitch.api.reference.entities.responses.twitch.ClipCreation;
import fr.funixgaming.twitch.api.reference.entities.responses.twitch.Game;
import fr.funixgaming.twitch.api.reference.entities.responses.twitch.User;
import fr.funixgaming.twitch.api.tools.HttpCalls;
import lombok.AllArgsConstructor;
import lombok.NonNull;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.Instant;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import static fr.funixgaming.twitch.api.tools.HttpCalls.HttpType;
import static fr.funixgaming.twitch.api.tools.HttpCalls.HttpJSONResponse;

@AllArgsConstructor
public class TwitchApi {

    private final static String PATH_CHANNEL = TwitchResources.TWITCH_API_PATH + "/channels";
    private final static String PATH_CHANNEL_POINTS = TwitchResources.TWITCH_API_PATH + "/channel_points/custom_rewards";
    private final static String PATH_CHANNEL_CHAT = TwitchResources.TWITCH_API_PATH + "/chat/emotes";
    private final static String PATH_CHANNEL_CLIP = TwitchResources.TWITCH_API_PATH + "/clips";
    private final static String PATH_CHANNEL_GAMES = TwitchResources.TWITCH_API_PATH + "/games";
    private final static String PATH_CHANNEL_STREAMS = TwitchResources.TWITCH_API_PATH + "/streams";
    private final static String PATH_CHANNEL_USERS = TwitchResources.TWITCH_API_PATH + "/users";

    private final TwitchAuth twitchAuth;

    public Channel getChannelInformation(@NonNull final String channelId) throws IOException {
        try {
            if (!twitchAuth.isValid()) {
                twitchAuth.refresh();
            }

            final URI url = new URI(
                    "https",
                    TwitchResources.DOMAIN_TWITCH_API,
                    PATH_CHANNEL,
                    "broadcaster_id=" + channelId,
                    null
            );

            final HttpJSONResponse response = HttpCalls.performJSONRequest(url, HttpType.GET, null, twitchAuth);
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

    /**
     * TODO Not working, need to fetch token from a login twitch page
     */
    public void updateChannelInformation(@NonNull final String channelId, @NonNull final UpdateChannel updateChannel) throws IOException {
        try {
            if (!twitchAuth.isValid()) {
                twitchAuth.refresh();
            }

            final URI url = new URI(
                    "https",
                    TwitchResources.DOMAIN_TWITCH_API,
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

            final HttpJSONResponse response = HttpCalls.performJSONRequest(url, HttpType.PATCH, body.toString(), twitchAuth);
            if (response.getResponseCode() != 204) {
                throw new IOException("An error occurred while updating channelId " + channelId + ".\nHttp error code : " + response.getResponseCode() + "\nBody : " + response.getBody());
            }
        } catch (URISyntaxException err) {
            throw new IOException(err);
        }
    }

    /**
     * TODO Not working, need to fetch token from a login twitch page
     */
    public Set<ChannelReward> getChannelCustomRewards(@NonNull final String channelId) throws IOException {
        try {
            if (!twitchAuth.isValid()) {
                twitchAuth.refresh();
            }

            final URI url = new URI(
                    "https",
                    TwitchResources.DOMAIN_TWITCH_API,
                    PATH_CHANNEL_POINTS,
                    "broadcaster_id=" + channelId,
                    null
            );

            final HttpJSONResponse response = HttpCalls.performJSONRequest(url, HttpType.GET, null, twitchAuth);
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
                }
                return rewards;
            } else {
                throw new IOException("An error occurred while fetching channel rewards. Http Error code : " + response.getResponseCode());
            }
        } catch (URISyntaxException err) {
            throw new IOException(err);
        }
    }

    public Set<ChannelEmotes> getChannelEmotes(@NonNull final String channelId) throws IOException {
        try {
            if (!twitchAuth.isValid()) {
                twitchAuth.refresh();
            }

            final URI url = new URI(
                    "https",
                    TwitchResources.DOMAIN_TWITCH_API,
                    PATH_CHANNEL_CHAT,
                    "broadcaster_id=" + channelId,
                    null
            );

            final HttpJSONResponse response = HttpCalls.performJSONRequest(url, HttpType.GET, null, twitchAuth);
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
                throw new IOException("An error occurred while fetching channel emotes.\nHttp Error code : " + response.getResponseCode() + "\nBody: " + response.getBody());
            }
        } catch (URISyntaxException e) {
            throw new IOException(e);
        }
    }

    public ClipCreation createClip(@NonNull final String channelId) throws IOException {
        try {
            if (!twitchAuth.isValid()) {
                twitchAuth.refresh();
            }

            final URI url = new URI(
                    "https",
                    TwitchResources.DOMAIN_TWITCH_API,
                    PATH_CHANNEL_CLIP,
                    "broadcaster_id=" + channelId,
                    null
            );

            final HttpJSONResponse response = HttpCalls.performJSONRequest(url, HttpType.POST, null, twitchAuth);
            if (response.getResponseCode() == 200) {
                final JsonObject clip = response.getBody().getAsJsonObject().get("data").getAsJsonArray().get(0).getAsJsonObject();
                return new ClipCreation(
                        clip.get("id").getAsString(),
                        clip.get("edit_url").getAsString()
                );
            } else {
                throw new IOException("An error occurred while creating a clip on channel " + channelId + ".\nError code : " + response.getResponseCode() + "\nBody : " + response.getBody());
            }
        } catch (URISyntaxException e) {
            throw new IOException(e);
        }
    }

    public Set<Clip> getChannelClips(@NonNull final String channelId, final ClipSearch search) throws IOException {
        try {
            if (!twitchAuth.isValid()) {
                twitchAuth.refresh();
            }
            final Set<String> searchQuery = new HashSet<>();

            if (search != null) {
                if (search.getNumberOfClips() != null) {
                    searchQuery.add("first=" + search.getNumberOfClips());
                }
                if (search.getClipAfterCursor() != null) {
                    searchQuery.add("after=" + search.getClipAfterCursor());
                }
                if (search.getClipBeforeCursor() != null) {
                    searchQuery.add("before=" + search.getClipBeforeCursor());
                }
                if (search.getEndedAtSearch() != null) {
                    searchQuery.add("ended_at=" + TwitchResources.dateToRFC3339(search.getEndedAtSearch()));
                }
                if (search.getStartedAtSearch() != null) {
                    searchQuery.add("started_at=" + TwitchResources.dateToRFC3339(search.getStartedAtSearch()));
                }
            }

            final URI url = new URI(
                    "https",
                    TwitchResources.DOMAIN_TWITCH_API,
                    PATH_CHANNEL_CLIP,
                    "broadcaster_id=" + channelId + (searchQuery.size() > 0 ? "&" + String.join("&", searchQuery) : ""),
                    null
            );

            final HttpJSONResponse response = HttpCalls.performJSONRequest(url, HttpType.GET, null, twitchAuth);
            if (response.getResponseCode() == 200) {
                final Set<Clip> clips = new HashSet<>();
                final JsonObject body = response.getBody().getAsJsonObject();
                final JsonArray clipsGet = body.get("data").getAsJsonArray();
                final JsonElement pagination = body.get("pagination");
                String cursor = null;

                if (!pagination.isJsonNull()) {
                    cursor = pagination.getAsJsonObject().get("cursor").getAsString();
                }

                for (final JsonElement clip : clipsGet) {
                    final JsonObject data = clip.getAsJsonObject();

                    clips.add(new Clip(
                            data.get("id").getAsString(),
                            data.get("url").getAsString(),
                            data.get("embed_url").getAsString(),
                            data.get("broadcaster_id").getAsString(),
                            data.get("broadcaster_name").getAsString(),
                            data.get("creator_id").getAsString(),
                            data.get("creator_name").getAsString(),
                            data.get("video_id").getAsString(),
                            data.get("game_id").getAsString(),
                            data.get("language").getAsString(),
                            data.get("title").getAsString(),
                            data.get("view_count").getAsInt(),
                            TwitchResources.rfc3339ToDate(data.get("created_at").getAsString()),
                            data.get("thumbnail_url").getAsString(),
                            data.get("duration").getAsFloat(),
                            cursor
                    ));
                }
                return clips;
            } else {
                throw new IOException("An error occurred while fetching clips on channel " + channelId + ". Error code : " + response.getResponseCode());
            }
        } catch (URISyntaxException e) {
            throw new IOException(e);
        }
    }

    public Game getGameById(final String gameId) throws IOException {
        return getGame(gameId, true);
    }

    public Game getGameByName(final String gameName) throws IOException {
        return getGame(gameName, false);
    }

    public Set<Stream> getStreamsByUserNames(final Set<String> userNames) throws IOException {
        return getStreamInfo(userNames, false);
    }

    public Set<Stream> getStreamsById(final Set<String> idList) throws IOException {
        return getStreamInfo(idList, true);
    }

    public Set<User> getUsersByUserName(final Set<String> userNames) throws IOException {
        return getUsers(userNames, false);
    }

    public Set<User> getUsersById(final Set<String> idList) throws IOException {
        return getUsers(idList, true);
    }

    private Set<User> getUsers(final Set<String> userList, boolean isID) throws IOException {
        try {
            if (userList.isEmpty()) {
                throw new IOException("Users list empty");
            }
            if (!twitchAuth.isValid()) {
                twitchAuth.refresh();
            }

            userList.forEach(user -> user = user.toLowerCase());
            final String searchQuery = (isID ? "id" : "login") + '=';
            final URI url = new URI(
                    "https",
                    TwitchResources.DOMAIN_TWITCH_API,
                    PATH_CHANNEL_USERS,
                    searchQuery + String.join("&" + searchQuery, userList),
                    null
            );
            final HttpJSONResponse response = HttpCalls.performJSONRequest(url, HttpType.GET, null, twitchAuth);
            if (response.getResponseCode() == 200) {
                final Set<User> users = new HashSet<>();
                final JsonArray fetched = response.getBody().getAsJsonObject().get("data").getAsJsonArray();

                for (final JsonElement element : fetched) {
                    final JsonObject data = element.getAsJsonObject();
                    final User.BroadcasterType broadcasterType;
                    final String bdType = data.get("broadcaster_type").getAsString();

                    if (bdType.equals("partner")) {
                        broadcasterType = User.BroadcasterType.PARTNER;
                    } else if (bdType.equals("affiliate")) {
                        broadcasterType = User.BroadcasterType.AFFILIATE;
                    } else {
                        broadcasterType = User.BroadcasterType.NORMAL;
                    }

                    users.add(new User(
                            broadcasterType,
                            data.get("description").getAsString(),
                            data.get("display_name").getAsString(),
                            data.get("id").getAsString(),
                            data.get("login").getAsString(),
                            data.get("offline_image_url").getAsString(),
                            data.get("profile_image_url").getAsString(),
                            data.get("view_count").getAsInt(),
                            Date.from(Instant.parse(data.get("created_at").getAsString()))
                    ));
                }
                return users;
            } else {
                throw new IOException("An error occurred while fetching users.\nHttp error code : " + response.getResponseCode() + "\nBody : " + response.getBody());
            }
        } catch (URISyntaxException e) {
            throw new IOException(e);
        }
    }

    private Set<Stream> getStreamInfo(final Set<String> userList, boolean isID) throws IOException {
        try {
            if (userList.isEmpty()) {
                throw new IOException("Streamer list empty");
            }
            if (!twitchAuth.isValid()) {
                twitchAuth.refresh();
            }

            userList.forEach(user -> user = user.toLowerCase());
            final String searchQuery = (isID ? "user_id" : "user_login") + '=';

            final URI url = new URI(
                    "https",
                    TwitchResources.DOMAIN_TWITCH_API,
                    PATH_CHANNEL_STREAMS,
                    searchQuery + String.join("&" + searchQuery, userList),
                    null
            );
            final HttpJSONResponse response = HttpCalls.performJSONRequest(url, HttpType.GET, null, twitchAuth);
            if (response.getResponseCode() == 200) {
                final Set<Stream> streams = new HashSet<>();
                final JsonArray data = response.getBody().getAsJsonObject().get("data").getAsJsonArray();

                for (final JsonElement elem : data) {
                    final JsonObject stream = elem.getAsJsonObject();
                    streams.add(new Stream(
                            stream.get("id").getAsString(),
                            stream.get("user_id").getAsString(),
                            stream.get("user_login").getAsString(),
                            stream.get("user_name").getAsString(),
                            stream.get("game_id").getAsString(),
                            stream.get("game_name").getAsString(),
                            stream.get("title").getAsString(),
                            stream.get("viewer_count").getAsInt(),
                            Date.from(Instant.parse(stream.get("started_at").getAsString())),
                            stream.get("language").getAsString(),
                            stream.get("thumbnail_url").getAsString(),
                            stream.get("is_mature").getAsBoolean()
                    ));
                }
                return streams;
            } else {
                throw new IOException("An error occurred while fetching streams.\nHttp error code : " + response.getResponseCode() + "\nBody : " + response.getBody());
            }
        } catch (URISyntaxException e) {
            throw new IOException(e);
        }
    }

    private Game getGame(final String data, boolean isID) throws IOException {
        try {
            if (!twitchAuth.isValid()) {
                twitchAuth.refresh();
            }

            final URI url = new URI(
                    "https",
                    TwitchResources.DOMAIN_TWITCH_API,
                    PATH_CHANNEL_GAMES,
                    (isID ? "id" : "name") + "=" + data,
                    null
            );

            final HttpJSONResponse response = HttpCalls.performJSONRequest(url, HttpType.GET, null, twitchAuth);
            if (response.getResponseCode() == 200) {
                final JsonObject dataGet = response.getBody().getAsJsonObject().get("data").getAsJsonArray().get(0).getAsJsonObject();

                return new Game(
                        dataGet.get("id").getAsString(),
                        dataGet.get("name").getAsString(),
                        dataGet.get("box_art_url").getAsString()
                );
            } else {
                throw new IOException("An error occurred while fetching game " + data + ".\nHttp error code : " + response.getResponseCode() + "\nBody : " + response.getBody());
            }
        } catch (IndexOutOfBoundsException e) {
            return null;
        } catch (URISyntaxException err) {
            throw new IOException(err);
        }
    }

}
