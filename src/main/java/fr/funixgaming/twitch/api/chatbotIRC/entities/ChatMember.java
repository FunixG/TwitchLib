package fr.funixgaming.twitch.api.chatbotIRC.entities;

public class ChatMember extends User {

    protected final User user;
    protected final UserBadges badges;
    protected final int roomID;
    protected final String channelName;

    public ChatMember(final User user,
                      final int roomID,
                      final String channelName,
                      final UserBadges badges) {
        super(user.getColor(), user.getDisplayName(), user.getLoginName(), user.getUserId());
        this.user = user;
        this.badges = badges;
        this.roomID = roomID;
        this.channelName = channelName;


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
