package fr.funixgaming.twitch.api.chatbot_irc.events;

import fr.funixgaming.twitch.api.chatbot_irc.TagParser;
import fr.funixgaming.twitch.api.chatbot_irc.TwitchBot;
import fr.funixgaming.twitch.api.chatbot_irc.entities.*;
import lombok.Getter;

@Getter
public class UserChatEvent extends TwitchEvent {

    private final ChatMember chatMember;
    private final ChatMessage chatMessage;

    public UserChatEvent(final TagParser parser, final TwitchBot bot) {
        super(bot);

        this.chatMember = new ChatMember(parser);
        this.chatMessage = new ChatMessage(parser, this.chatMember);
    }
}
