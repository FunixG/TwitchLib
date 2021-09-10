package fr.funixgaming.twitch.api.auth;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.util.Set;

@Getter
@Setter(AccessLevel.PROTECTED)
public abstract class AuthEntity {
    private String clientId;
    private String accessToken;
    private String clientSecret;
    private Set<String> scopes;

    abstract public void refresh() throws IOException;
    abstract public boolean isValid();
}
