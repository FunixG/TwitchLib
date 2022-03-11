package fr.funixgaming.twitch.api.auth;

import fr.funixgaming.twitch.api.exceptions.TwitchApiException;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;

public class TwitchAuthTestUtils {

    private static TwitchAuth auth = null;

    public static TwitchAuth getAuth() throws TwitchApiException {
        final String clientId = System.getenv("CLIENT_ID");
        final String clientSecret = System.getenv("CLIENT_SECRET");
        final String oauthCode = System.getenv("OAUTH_CODE");
        final String redirectUrl = System.getenv("REDIRECT_URI");

        if (auth == null) {
            final TwitchAuth twitchAuth = getFileAuth();

            if (twitchAuth == null) {
                auth = new TwitchAuth(
                        clientId,
                        clientSecret,
                        oauthCode,
                        redirectUrl
                );
            } else {
                auth = twitchAuth;
            }
        }

        saveAuthInFile();
        System.out.println("Logged user: " + auth.getUserName() + " id: " + auth.getUserId());
        return auth;
    }

    @Nullable
    private static TwitchAuth getFileAuth() throws TwitchApiException {
        final File authFile = getFile(false);

        try {
            if (authFile != null) {
                final String data = Files.readString(authFile.toPath());
                return TwitchAuth.fromJson(data);
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
        final File authFile = new File("twitchAuth.json");

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
