package fr.funixgaming.twitch.api.chatbot_irc.entities;

import fr.funixgaming.twitch.api.chatbot_irc.parsers.TagParser;
import lombok.Getter;

import java.util.Map;

@Getter
public class User {

    private final String color;
    private final String displayName;
    private final String loginName;
    private final int id;

    public User(final TagParser parser) {
        final Map<String, String> params = parser.getTagMap();

        this.color = params.getOrDefault("color", "");
        this.displayName = params.getOrDefault("display-name", "");
        this.loginName = params.getOrDefault("login", this.displayName);
        this.id = Integer.parseInt(params.get("user-id"));
    }

    public String getColor() {
        return color;
    }

    public String getDisplayName() {
        return displayName;
    }

    public int getUserId() {
        return id;
    }

    public String getLoginName() {
        return loginName;
    }
}
