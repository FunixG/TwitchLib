package fr.funixgaming.twitch.api.tools;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import fr.funixgaming.twitch.api.auth.AuthEntity;
import fr.funixgaming.twitch.api.auth.TwitchAuth;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

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

    public static HttpJSONResponse performJSONRequest(final URI uri,
                                                      final HttpType httpType,
                                                      String body,
                                                      final AuthEntity twitchAuth) throws IOException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder();

        if (body == null) {
            body = "";
        }

        requestBuilder.uri(uri);
        switch (httpType) {
            case GET:
                requestBuilder.GET();
                break;
            case POST:
                requestBuilder.POST(HttpRequest.BodyPublishers.ofString(body));
                break;
            case PATCH:
                requestBuilder.method(HttpType.PATCH.name(), HttpRequest.BodyPublishers.ofString(body));
                break;
        }
        if (twitchAuth != null) {
            requestBuilder.setHeader("Authorization", "Bearer " + twitchAuth.getAccessToken());
            requestBuilder.setHeader("Client-Id", twitchAuth.getClientId());
        }
        requestBuilder.setHeader("Content-Type", "application/json");
        requestBuilder.setHeader("Accept", "application/json");

        try {
            final HttpRequest request = requestBuilder.build();
            final HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            return new HttpJSONResponse(
                    JsonParser.parseString(response.body()),
                    response.statusCode()
            );
        } catch (InterruptedException e) {
            throw new IOException(e);
        }
    }

}
