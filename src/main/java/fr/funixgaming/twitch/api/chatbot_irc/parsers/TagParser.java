package fr.funixgaming.twitch.api.chatbot_irc.parsers;

import fr.funixgaming.twitch.api.chatbot_irc.enums.TwitchTag;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public class TagParser {

    private final Map<String, String> tagMap = new HashMap<>();
    private TwitchTag twitchTag;
    private String channel;
    private String message;

    public TagParser(String twitchString) {
        StringBuilder stringBuilder = new StringBuilder();
        String tags = null;
        String command = null;
        String channel = null;
        String message = null;
        boolean hasReadTwitchDomain = false;
        boolean readingTwitchDomain = false;

        if (twitchString.charAt(0) != '@') {
            tags = "";
            readingTwitchDomain = true;
        } else {
            twitchString = twitchString.substring(1);
        }

        for (char c : twitchString.toCharArray()) {
            if (c == ' ') {
                if (tags == null) {
                    tags = stringBuilder.toString();
                    stringBuilder = new StringBuilder();
                    readingTwitchDomain = true;
                    continue;
                } else if (!hasReadTwitchDomain) {
                    hasReadTwitchDomain = true;
                    readingTwitchDomain = false;
                    continue;
                } else if (command == null) {
                    command = stringBuilder.toString();
                    stringBuilder = new StringBuilder();
                    continue;
                } else if (channel == null) {
                    channel = stringBuilder.toString();
                    stringBuilder = new StringBuilder();
                    continue;
                }
            }
            if (!readingTwitchDomain)
                stringBuilder.append(c);
        }
        if (stringBuilder.length() > 0) {
            if (channel == null)
                channel = stringBuilder.toString();
            else
                message = stringBuilder.toString();
        }

        this.channel = channel;
        this.message = message;

        try {
            if (command == null) {
                this.twitchTag = null;
            } else {
                this.twitchTag = TwitchTag.valueOf(command);
                for (final String tagElem : tags.split(";")) {
                    final String[] tag = tagElem.split("=");

                    if (tag.length == 2 && this.twitchTag.getTags().contains(tag[0])) {
                        this.tagMap.put(tag[0], tag[1]);
                    }
                }
            }
            if (this.message != null && this.message.startsWith(":"))
                this.message = this.message.substring(1);
            if (this.channel != null && this.channel.startsWith("#"))
                this.channel = this.channel.substring(1);
        } catch (IllegalArgumentException e) {
            this.twitchTag = null;
        }
    }
}
