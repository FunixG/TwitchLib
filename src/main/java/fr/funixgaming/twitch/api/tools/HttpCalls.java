package fr.funixgaming.twitch.api.tools;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import fr.funixgaming.twitch.api.auth.TwitchAuth;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class HttpCalls {

    private static final String EVENT_SUB_ENDPOINT = "https://api.twitch.tv/helix/eventsub/subscriptions";

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

    /**
     * https://dev.twitch.tv/docs/eventsub#subscriptions
     * @param clientId App client id
     * @param bearerToken bearer app token
     * @param jsonBodyEventSubRegister json body of the register event
     * @return HTTP response code and body json
     * @throws IOException Throws when a http error occurs
     */
    @NonNull
    public static HttpJSONResponse performEventSubRegister(@NonNull final String clientId,
                                                           @NonNull final String bearerToken,
                                                           @NonNull final String jsonBodyEventSubRegister) throws IOException {
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder();

        requestBuilder.uri(URI.create(EVENT_SUB_ENDPOINT));
        requestBuilder.POST(HttpRequest.BodyPublishers.ofString(jsonBodyEventSubRegister));
        requestBuilder.setHeader("Client-Id", clientId);
        requestBuilder.setHeader("Authorization", "Bearer " + bearerToken);
        requestBuilder.setHeader("Content-Type", "application/json");

        return sendHttpClientRequest(requestBuilder);
    }

    public static void performEventSubUnRegister(@NonNull final String clientId,
                                                 @NonNull final String bearerToken,
                                                 @NonNull final String eventIdToRemove) throws IOException {
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder();

        requestBuilder.uri(URI.create(EVENT_SUB_ENDPOINT + "?id=" + eventIdToRemove));
        requestBuilder.DELETE();
        requestBuilder.setHeader("Client-Id", clientId);
        requestBuilder.setHeader("Authorization", "Bearer " + bearerToken);

        HttpJSONResponse response = sendHttpClientRequest(requestBuilder);
        if (response.getResponseCode() != 204) {
            throw new IOException("Could not remove subscription: " + eventIdToRemove + "\nCode: " + response.getResponseCode() + "\nBody: " + response.getBody());
        }
    }

    public static HttpJSONResponse performFormRequest(@NonNull final URI uri,
                                                      @NonNull final HttpType httpType,
                                                      @Nullable String body,
                                                      final TwitchAuth twitchAuth) throws IOException {
        final HttpRequest.Builder requestBuilder = getHttpBuilder(body, uri, httpType, twitchAuth);

        requestBuilder.setHeader("Content-Type", "application/x-www-form-urlencoded");
        requestBuilder.setHeader("Accept", "application/json");

        return sendHttpClientRequest(requestBuilder);
    }

    public static HttpJSONResponse performJSONRequest(@NonNull final URI uri,
                                                      @NonNull final HttpType httpType,
                                                      @Nullable String body,
                                                      @Nullable final TwitchAuth twitchAuth) throws IOException {
        final HttpRequest.Builder requestBuilder = getHttpBuilder(body, uri, httpType, twitchAuth);

        requestBuilder.setHeader("Content-Type", "application/json");
        requestBuilder.setHeader("Accept", "application/json");

        return sendHttpClientRequest(requestBuilder);
    }

    private static HttpRequest.Builder getHttpBuilder(@Nullable String body,
                                                      @NonNull final URI uri,
                                                      @NonNull final HttpType httpType,
                                                      @Nullable final TwitchAuth twitchAuth) {
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder();

        if (body == null) {
            body = "";
        }

        requestBuilder.uri(uri);

        switch (httpType) {
            case GET -> requestBuilder.GET();
            case POST -> requestBuilder.POST(HttpRequest.BodyPublishers.ofString(body));
            case PATCH -> requestBuilder.method(HttpType.PATCH.name(), HttpRequest.BodyPublishers.ofString(body));
        }

        if (twitchAuth != null) {
            requestBuilder.setHeader("Authorization", "Bearer " + twitchAuth.getAccessToken());
            requestBuilder.setHeader("Client-Id", twitchAuth.getClientId());
        }
        return requestBuilder;
    }

    private static HttpJSONResponse sendHttpClientRequest(HttpRequest.Builder requestBuilder) throws IOException {
        try {
            final HttpRequest request = requestBuilder.build();
            final HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());

            return new HttpJSONResponse(
                    JsonParser.parseString(response.body()),
                    response.statusCode()
            );
        } catch (InterruptedException e) {
            throw new IOException(e);
        }
    }

}
