package fr.funixgaming.twitch.api.chatbot_irc;

import fr.funixgaming.twitch.api.auth.TwitchAuth;
import fr.funixgaming.twitch.api.auth.TwitchAuthTestUtils;
import fr.funixgaming.twitch.api.exceptions.TwitchApiException;
import fr.funixgaming.twitch.api.exceptions.TwitchIRCException;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
class TestBot {
    private String botUsername;
    private TwitchAuth twitchAuth;

    private void startBot() {
        final String channel = "funixbot";

        final Thread botThread = new Thread(() -> {
            try {
                final TwitchBot twitchBot = new TwitchBot(this.botUsername, this.twitchAuth);
                twitchBot.addEventListener(new TestBotEvents());
                twitchBot.joinChannel(channel);

                twitchBot.sendMessageToChannel(channel, "test");

                while (twitchBot.isConnected()) {
                    twitchBot.sendMessageToChannel(channel, UUID.randomUUID().toString());
                    Thread.sleep(100);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        botThread.start();
    }

    public static void main(String[] args) throws TwitchApiException {
        final TestBot main = new TestBot();

        main.setBotUsername("testfunix");
        main.twitchAuth = TwitchAuthTestUtils.getAuth();
        main.startBot();
    }

}
