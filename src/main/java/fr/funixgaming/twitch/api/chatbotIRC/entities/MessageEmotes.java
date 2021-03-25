package fr.funixgaming.twitch.api.chatbotIRC.entities;

import java.util.HashSet;
import java.util.Set;

public class MessageEmotes {

    private final static String URL_TWITCH_EMOTES = "http://static-cdn.jtvnw.net/emoticons/v1/";
    public enum EmoteSize {
        SMALL,
        MEDIUM,
        LARGE
    }

    private final Set<Integer> emotesID = new HashSet<>();
    private int emotesNumber = 0;

    public MessageEmotes(final String emotesString) {
        if (emotesString.isEmpty()) return;

        for (final String emoteSet : emotesString.split("/")) {
            final String[] emoteData = emoteSet.split(":");

            if (emoteData.length == 2) {
                this.emotesID.add(Integer.parseInt(emoteData[0]));
                emotesNumber += emoteData[1].split(",").length;
            }
        }
    }

    public int countEmotes() {
        return emotesNumber;
    }

    public Set<Integer> getEmotesID() {
        return emotesID;
    }

    public static String getTwitchEmoteURLByID(final int emoteID, final EmoteSize size) {
        switch (size) {
            case SMALL:
                return URL_TWITCH_EMOTES + emoteID + "/1.0";
            case MEDIUM:
                return URL_TWITCH_EMOTES + emoteID + "/2.0";
            case LARGE:
                return URL_TWITCH_EMOTES + emoteID + "/3.0";
            default:
                return null;
        }
    }

}
