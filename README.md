# TwitchLib

[![Maven Central](https://img.shields.io/maven-central/v/fr.funixgaming.twitch.api/twitch-api.svg)](https://search.maven.org/artifact/fr.funixgaming.twitch.api/twitch-api)
[![TwitchAPI Tests](https://github.com/FunixG/TwitchLib/actions/workflows/test-build.yml/badge.svg)](https://github.com/FunixG/TwitchLib/actions/workflows/test-build.yml)

![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=java&logoColor=white)

[![Twitch](https://img.shields.io/badge/Twitch-9146FF?style=for-the-badge&logo=twitch&logoColor=white)](https://twitch.tv/funixgaming)
[![YouTube](https://img.shields.io/badge/YouTube-FF0000?style=for-the-badge&logo=youtube&logoColor=white)](https://youtube.com/c/funixgaming)
[![Twitter](https://img.shields.io/badge/Twitter-1DA1F2?style=for-the-badge&logo=twitter&logoColor=white)](https://twitter.com/funixgaming)

Project used to create my self Twitch library to build IRC Bots &amp; call the Twitch API much easier and faster

### Importing

````xml
<dependency>
    <groupId>fr.funixgaming.twitch.api</groupId>
    <artifactId>twitch-api</artifactId>
    <version>(version)</version>
</dependency>
````

### Build a twitch bot

#### Create bot instance
````java
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

                //example command to send chat message to channel
                twitchBot.sendMessageToChannel(channel, "test");

                while (twitchBot.isConnected()) {
                    //do your code
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
````

#### List of events for twitch
````java
public interface TwitchEvents {
    /**
     * Triggered when a streamer is hosting a channel
     * It's also triggered when a streamer stop hosting
     * @param event
     */
    default void onChannelHost(HostChannelEvent event) {};

    /**
     * Triggered when all user messages are removed (performed when a user is banned)
     * @param event
     */
    default void onClearUserMessages(ClearUserMessagesEvent event) {};

    /**
     * Triggered when a single message is deleted of a user
     * @param event
     */
    default void onMessageDeleted(DeleteMessageEvent event) {};

    /**
     * Triggered when the bot app is successfully logged to Twitch
     * @param event
     */
    default void onUserChat(UserChatEvent event) {};

    /**
     * Triggered when a channel activate the submode, follow only mode,
     * slow mode, emote only mode or the r9k mode
     * @param event
     */
    default void onRoomStateChange(RoomStateChangeEvent event) {};

    /**
     * When an user has subscibed to a channel or resub
     * @param event
     */
    default void onNewSubscription(NewSubscriptionEvent event) {};

    /**
     * When an subgift is done on a twitch channel
     * @param event
     */
    default void onNewSubscriptionGift(NewSubscriptionGiftEvent event) {};

    /**
     * When a channel raid another channel
     * @param event
     */
    default void onIncomingRaid(IncomingRaidEvent event) {};
}
````

#### Example implementation of auth
````java
public class TwitchAuthTestUtils {

    private static TwitchAuth auth = null;

    public static TwitchAuth getAuth() throws TwitchApiException {
        final String clientId = System.getenv("TWITCH_CLIENT_ID");
        final String clientSecret = System.getenv("TWITCH_CLIENT_SECRET");
        final String oauthCode = System.getenv("TWITCH_OAUTH_CODE");
        final String redirectUrl = System.getenv("TWITCH_REDIRECT_URL");
        final String accessToken = System.getenv("TWITCH_ACCESS_TOKEN");
        final String refreshToken = System.getenv("TWITCH_REFRESH_TOKEN");

        if (auth == null) {
            final TwitchAuth twitchAuth = getFileAuth(clientId, clientSecret);

            if (twitchAuth == null) {
                if (accessToken != null && refreshToken != null) {
                    auth = new TwitchAuth(
                            clientId,
                            clientSecret,
                            oauthCode,
                            accessToken,
                            refreshToken,
                            Date.from(Instant.now())
                    );
                } else {
                    auth = new TwitchAuth(
                            clientId,
                            clientSecret,
                            oauthCode,
                            redirectUrl
                    );
                }
            } else {
                Logger.getGlobal().log(Level.INFO, "Loading file twitch credentials.");
                auth = twitchAuth;
            }
        }

        saveAuthInFile();
        return auth;
    }

    @Nullable
    private static TwitchAuth getFileAuth(final String clientId, final String clientSecret) throws TwitchApiException {
        final File authFile = getFile(false);

        try {
            if (authFile != null) {
                final String data = Files.readString(authFile.toPath());
                return TwitchAuth.fromJson(data, clientId, clientSecret);
            } else {
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static void saveAuthInFile() {
        final File authFile = getFile(true);

        try {
            if (authFile != null) {
                Files.writeString(authFile.toPath(), auth.toJson(true), StandardOpenOption.TRUNCATE_EXISTING);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Nullable
    private static File getFile(boolean createIfAbsent) {
        final File data = new File("data");
        final File authFile = new File(data,"twitchAuth.json");

        if (!data.exists() && !data.mkdir()) {
            new IOException("Impossible de créer le data folder.").printStackTrace();
            return null;
        }

        if (authFile.exists()) {
            return authFile;
        } else {
            try {
                if (createIfAbsent) {
                    if (authFile.createNewFile()) {
                        return authFile;
                    } else {
                        throw new IOException("Could not create file. twitchAuth.json alerady exists");
                    }
                } else {
                    return null;
                }
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
    }

}

````