package fr.funixgaming.twitch.api.chatbotIRC.entities;

public class ChatMember extends User {

    protected final User user;
    protected final String badges;
    protected final int roomID;
    protected final String channelName;

    private boolean isStreamer = false;
    private boolean isModerator = false;
    private boolean isSubscriber = false;
    private boolean isVIP = false;
    private boolean isTwitchStaff = false;
    private boolean isTwitchAdmin = false;
    private boolean isTwitchPartner = false;
    private boolean isPrimeUser = false;
    private boolean isTurboUser = false;

    public ChatMember(final User user,
                      final String badges,
                      final int roomID,
                      final String channelName) {
        super(user.getColor(), user.getDisplayName(), user.getUserId());
        this.user = user;
        this.badges = badges;
        this.roomID = roomID;
        this.channelName = channelName;

        for (final String badgeInfo : badges.split(",")) {
            final String[] badgeData = badgeInfo.split("/");

            if (badgeData.length > 0) {
                switch (badgeData[0]) {
                    case "broadcaster":
                        this.isStreamer = true;
                        break;
                    case "moderator":
                        this.isModerator = true;
                        break;
                    case "subscriber":
                        this.isSubscriber = true;
                        break;
                    case "vip":
                        this.isVIP = true;
                        break;
                    case "premium":
                        this.isPrimeUser = true;
                        break;
                    case "staff":
                        this.isTwitchStaff = true;
                        break;
                    case "admin":
                        this.isTwitchAdmin = true;
                        break;
                    case "partner":
                        this.isTwitchPartner = true;
                        break;
                    case "turbo":
                        this.isTurboUser = true;
                        break;
                }
            }
        }
    }

    public boolean isStreamer() {
        return isStreamer;
    }

    public boolean isModerator() {
        return isModerator;
    }

    public boolean isSubscriber() {
        return isSubscriber;
    }

    public boolean isVIP() {
        return isVIP;
    }

    public boolean isTwitchStaff() {
        return isTwitchStaff;
    }

    public boolean isTwitchAdmin() {
        return isTwitchAdmin;
    }

    public boolean isTwitchPartner() {
        return isTwitchPartner;
    }

    public boolean isPrimeUser() {
        return isPrimeUser;
    }

    public boolean isTurboUser() {
        return isTurboUser;
    }

    public String getChannelName() {
        return channelName;
    }

    public int getRoomID() {
        return roomID;
    }
}
