package fr.funixgaming.twitch.api.reference;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import fr.funixgaming.twitch.api.exceptions.TwitchApiException;
import fr.funixgaming.twitch.api.tools.HttpCalls;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class TmiTwitchApi {

    public List<String> getUsernamesConnectedOnChat(final String streamer) throws TwitchApiException {
        try {
            final HttpCalls.HttpJSONResponse jsonResponse = HttpCalls.performJSONRequest(
                    URI.create(String.format("https://tmi.twitch.tv/group/user/%s/chatters", streamer)),
                    HttpCalls.HttpType.GET,
                    null,
                    null
            );
            final List<String> toRet = new ArrayList<>();
            final JsonObject res = jsonResponse.getBody().getAsJsonObject();
            final JsonObject chatters = res.get("chatters").getAsJsonObject();

            for (final JsonElement user : chatters.get("broadcaster").getAsJsonArray()) {
                toRet.add(user.getAsString());
            }
            for (final JsonElement user : chatters.get("vips").getAsJsonArray()) {
                toRet.add(user.getAsString());
            }
            for (final JsonElement user : chatters.get("moderators").getAsJsonArray()) {
                toRet.add(user.getAsString());
            }
            for (final JsonElement user : chatters.get("staff").getAsJsonArray()) {
                toRet.add(user.getAsString());
            }
            for (final JsonElement user : chatters.get("admins").getAsJsonArray()) {
                toRet.add(user.getAsString());
            }
            for (final JsonElement user : chatters.get("global_mods").getAsJsonArray()) {
                toRet.add(user.getAsString());
            }
            for (final JsonElement user : chatters.get("viewers").getAsJsonArray()) {
                toRet.add(user.getAsString());
            }

            return toRet;
        } catch (IOException | JsonParseException e) {
            throw new TwitchApiException(String.format("Error occured while getting chatters on stream %s", streamer), e);
        }
    }

}
