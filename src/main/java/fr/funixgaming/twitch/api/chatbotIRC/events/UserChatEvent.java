package fr.funixgaming.twitch.api.chatbotIRC.events;

import fr.funixgaming.twitch.api.chatbotIRC.TagParser;
import fr.funixgaming.twitch.api.chatbotIRC.TwitchBot;
import fr.funixgaming.twitch.api.chatbotIRC.entities.*;

import java.util.Map;

public class UserChatEvent extends TwitchEvent {

    private final User user;
    private final ChatMember chatMember;
    private final ChatMessage chatMessage;

    public UserChatEvent(final TagParser parser, final TwitchBot bot) {
        super(bot);
        final Map<String, String> params = parser.getTagMap();

        this.user = new User(params.get("color"),
                params.get("display-name"),
                Integer.parseInt(params.get("user-id")));
        this.chatMember = new ChatMember(this.user,
                Integer.parseInt(params.get("room-id")),
                parser.getChannel(),
                new UserBadges(params.getOrDefault("badges", "")));
        this.chatMessage = new ChatMessage(this.chatMember,
                parser.getMessage(),
                Long.parseLong(params.get("tmi-sent-ts")),
                params.get("id"),
                new MessageEmotes(params.getOrDefault("emotes", "")));
    }

    public User getUser() {
        return this.user;
    }

    public ChatMember getChatMember() {
        return this.chatMember;
    }

    public ChatMessage getMessage() {
        return this.chatMessage;
    }
}
