package fr.funixgaming.twitch.api.chatbot_irc.entities;

import fr.funixgaming.twitch.api.chatbot_irc.TagParser;
import lombok.Getter;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class MessageEmotes {

    @Getter private final Set<Integer> emotesID = new HashSet<>();
    @Getter private int emotesNumber = 0;

    private final Set<String> emotesIndexes = new HashSet<>();

    public MessageEmotes(final TagParser parser) {
        final String emotesString = parser.getTagMap().getOrDefault("emotes", "");

        if (emotesString.isEmpty()) return;

        for (final String emoteSet : emotesString.split("/")) {
            final String[] emoteData = emoteSet.split(":");

            if (emoteData.length == 2) {
                final String[] emotesPoses = emoteData[1].split(",");

                this.emotesID.add(Integer.parseInt(emoteData[0]));
                this.emotesIndexes.addAll(Arrays.asList(emotesPoses));
                emotesNumber += emotesPoses.length;
            }
        }
    }

}
