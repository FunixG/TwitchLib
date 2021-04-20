package fr.funixgaming.twitch.api.chatbotIRC.entities;

import fr.funixgaming.twitch.api.chatbotIRC.TagParser;

public class ChatMember extends User {

    protected final UserBadges badges;
    protected final int roomID;
    protected final String channelName;

    public ChatMember(final TagParser parser) {
        super(parser);
        this.badges = new UserBadges(parser);
        this.roomID = Integer.parseInt(parser.getTagMap().get("room-id"));
        this.channelName = parser.getChannel();
    }

    public String getChannelName() {
        return channelName;
    }

    public int getRoomID() {
        return roomID;
    }

    public UserBadges getBadges() {
        return badges;
    }
}
