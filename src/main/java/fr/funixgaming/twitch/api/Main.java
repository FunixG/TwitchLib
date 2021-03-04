package fr.funixgaming.twitch.api;

import fr.funixgaming.twitch.api.chatbotIRC.TwitchChatBot;

public class Main {

    public static void main(String[] args) {
        final TwitchChatBot twitchChatBot = new TwitchChatBot(args[0], args[1]);
        twitchChatBot.joinChannel("solary");
        twitchChatBot.sendMessageToChannel("funixgaming", "test");
        while (twitchChatBot.isRunning());
    }

}
