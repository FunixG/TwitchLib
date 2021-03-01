package fr.funixgaming.twitch.api;

import fr.funixgaming.twitch.api.chatbotIRC.TwitchChatBot;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        final TwitchChatBot twitchChatBot = new TwitchChatBot("funixbot", "");
        twitchChatBot.joinChannel("funixgaming", "gotaga");
        twitchChatBot.sendMessageToChannel("funixgaming", "test");
        while (twitchChatBot.isRunning());
    }

}
