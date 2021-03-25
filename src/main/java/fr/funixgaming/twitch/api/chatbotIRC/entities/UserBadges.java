package fr.funixgaming.twitch.api.chatbotIRC.entities;

public class UserBadges {

    private boolean isStreamer = false;
    private boolean isModerator = false;
    private boolean isSubscriber = false;
    private boolean isVIP = false;
    private boolean isTwitchStaff = false;
    private boolean isTwitchAdmin = false;
    private boolean isTwitchPartner = false;
    private boolean isPrimeUser = false;
    private boolean isTurboUser = false;

    public UserBadges(final String badgesString) {
        if (badgesString.isEmpty()) return;

        for (final String badgeInfo : badgesString.split(",")) {
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

}
