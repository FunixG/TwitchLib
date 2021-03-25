package fr.funixgaming.twitch.api.chatbotIRC.events;

import fr.funixgaming.twitch.api.chatbotIRC.TagParser;
import fr.funixgaming.twitch.api.chatbotIRC.TwitchBot;
import fr.funixgaming.twitch.api.chatbotIRC.entities.*;

public class UserChatEvent extends TwitchEvent {

    private final User user;
    private final ChatMember chatMember;
    private final ChatMessage chatMessage;

    public UserChatEvent(final TagParser parser, final TwitchBot bot) {
        super(bot);

        this.user = new User(parser);
        this.chatMember = new ChatMember(parser);
        this.chatMessage = new ChatMessage(parser, this.chatMember);
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
