package fr.funixgaming.twitch.api.chatbotIRC.entities;

public class User {

    private final String color;
    private final String displayName;
    private final int id;

    public User(final String color,
            final String dispayName,
            final int id) {
        this.color = color;
        this.displayName = dispayName;
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
}
