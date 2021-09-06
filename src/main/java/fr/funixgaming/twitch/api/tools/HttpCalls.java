package fr.funixgaming.twitch.api.tools;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import fr.funixgaming.twitch.api.auth.TwitchAuth;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;

public class HttpCalls {

    public enum HttpType {
        GET,
        POST,
        PATCH
    }

    @Getter
    @AllArgsConstructor
    public static class HttpJSONResponse {
        private final JsonElement body;
        private final Integer responseCode;
    }

    public static HttpJSONResponse performJSONRequest(final URL url,
                                                      final HttpType httpType,
                                                      final String body,
                                                      final TwitchAuth twitchAuth) throws IOException {
        HttpURLConnection connection = null;
        OutputStream outputStream = null;
        OutputStreamWriter outputStreamWriter = null;
        InputStreamReader inputStreamReader = null;

        try {
            final URLConnection urlConnection = url.openConnection();

            connection = (HttpURLConnection) urlConnection;

            if (twitchAuth != null) {
                connection.setRequestProperty("Authorization", "Bearer " + twitchAuth.getAccessToken());
                connection.setRequestProperty("Client-Id", twitchAuth.getClientId());
            }

            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Accept", "application/json");

            connection.setRequestMethod(httpType.name());

            if (body != null) {
                connection.setDoOutput(true);
                outputStream = connection.getOutputStream();
                outputStreamWriter = new OutputStreamWriter(outputStream, StandardCharsets.UTF_8);
                outputStreamWriter.write(new String(body.getBytes(StandardCharsets.ISO_8859_1)));
                outputStreamWriter.flush();
            }

            connection.connect();

            inputStreamReader = new InputStreamReader(connection.getInputStream());

            final JsonElement response = JsonParser.parseReader(inputStreamReader);
            final Integer responseCode = connection.getResponseCode();

            return new HttpJSONResponse(response, responseCode);
        } catch (IOException e) {
            if (connection != null) {
                throw new IOException(connection.getResponseMessage(), e);
            } else {
                throw e;
            }
        } finally {
            try {
                if (connection != null) {
                    connection.disconnect();
                }
                if (outputStream != null) {
                    outputStream.close();
                }
                if (outputStreamWriter != null) {
                    outputStreamWriter.close();
                }
                if (inputStreamReader != null) {
                    inputStreamReader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
