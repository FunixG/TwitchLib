package fr.funixgaming.twitch.api.chatbotIRC.entities;

public class User {

    private final String color;
    private final String displayName;
    private final String loginName;
    private final int id;

    public User(final String color,
            final String loginName,
            final String displayName,
            final int id) {
        this.color = color;
        this.displayName = displayName;
        this.loginName = loginName;
        this.id = id;
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
