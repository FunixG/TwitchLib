package fr.funixgaming.twitch.api;

import fr.funixgaming.twitch.api.chatbotIRC.TwitchChatBot;

public class Main {

    public static void main(String[] args) {
        final TwitchChatBot twitchChatBot = new TwitchChatBot("funixbot", args[0]);
        twitchChatBot.joinChannel("funixgaming", "gotaga");
        twitchChatBot.sendMessageToChannel("funixgaming", "test");
        while (twitchChatBot.isRunning());
    }

}
