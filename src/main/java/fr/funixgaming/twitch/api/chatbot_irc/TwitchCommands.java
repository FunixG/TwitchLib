package fr.funixgaming.twitch.api.chatbot_irc;

public class TwitchCommands {

    private final TwitchBot bot;

    protected TwitchCommands(final TwitchBot twitchBot) {
        this.bot = twitchBot;
    }

    public void banUser(final String channel, final String userName) {
        bot.sendMessage("PRIVMSG #" + channel + " :/ban " + userName);
    }

    public void timeOutUser(final String channel, final String userName, final int seconds) {
        bot.sendMessage("PRIVMSG #" + channel + " :/timeout " + userName + ' ' + seconds);
    }

    public void unBanUser(final String channel, final String userName) {
        bot.sendMessage("PRIVMSG #" + channel + " :/unban " + userName);
    }

    public void clearChat(final String channel) {
        bot.sendMessage("PRIVMSG #" + channel + " :/clear");
    }

    public void setColor(final String channel, final String color) {
        bot.sendMessage("PRIVMSG #" + channel + " :/color " + color);
    }

    /**
     * Start a pub on a channel
     * @param channel name channel
     * @param duration 30|60|90|120|150|180
     */
    public void startCommercial(final String channel, final int duration) {
        bot.sendMessage("PRIVMSG #" + channel + " :/commercial " + duration);
    }

    public void startCommercial(final String channel) {
        bot.sendMessage("PRIVMSG #" + channel + " :/commercial");
    }

    public void hostChannel(final String channelWhoHosts, final String channelToHost) {
        bot.sendMessage("PRIVMSG #" + channelWhoHosts + " :/host " + channelToHost);
    }

    public void stopHostingChannel(final String channelWhoHosts) {
        bot.sendMessage("PRIVMSG #" + channelWhoHosts + " :/unhost");
    }

    public void raidChannel(final String channelWhoRaids, final String channelToRaid) {
        bot.sendMessage("PRIVMSG #" + channelWhoRaids + " :/raid " + channelToRaid);
    }

    public void cancelRaid(final String channelWhoRaids) {
        bot.sendMessage("PRIVMSG #" + channelWhoRaids + " :/unraid");
    }

    public void addStreamMarker(final String channel, final String description) {
        bot.sendMessage("PRIVMSG #" + channel + " :/marker " + (description == null ? "" : description));
    }

    public void activateEmoteOnly(final String channel) {
        bot.sendMessage("PRIVMSG #" + channel + " :/emoteonly");
    }

    public void disableEmoteOnly(final String channel) {
        bot.sendMessage("PRIVMSG #" + channel + " :/emoteonlyoff");
    }

    public void activateUniqueChat(final String channel) {
        bot.sendMessage("PRIVMSG #" + channel + " :/uniquechat");
    }

    public void disableUniqueChat(final String channel) {
        bot.sendMessage("PRIVMSG #" + channel + " :/uniquechatoff");
    }

    public void activateSubOnly(final String channel) {
        bot.sendMessage("PRIVMSG #" + channel + " :/subscribers");
    }

    public void disableSubOnly(final String channel) {
        bot.sendMessage("PRIVMSG #" + channel + " :/subscribersoff");
    }

    public void activateFollowMode(final String channel) {
        bot.sendMessage("PRIVMSG #" + channel + " :/followers");
    }

    public void activateFollowMode(final String channel, final String duration) {
        bot.sendMessage("PRIVMSG #" + channel + " :/followers " + duration);
    }

    public void disableFollowMode(final String channel) {
        bot.sendMessage("PRIVMSG #" + channel + " :/followersoff");
    }

    public void activateSlowMode(final String channel, final int cooldown) {
        bot.sendMessage("PRIVMSG #" + channel + " :/slow " + cooldown);
    }

    public void disableSlowMode(final String channel) {
        bot.sendMessage("PRIVMSG #" + channel + " :/slowoff");
    }

}
