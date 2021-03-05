package fr.funixgaming.twitch.api.chatbotIRC.events;

import fr.funixgaming.twitch.api.chatbotIRC.TagParser;
import fr.funixgaming.twitch.api.chatbotIRC.TwitchBot;

public class RemoveUserMessageEvent extends TwitchEvent {

    public RemoveUserMessageEvent(final TagParser parser, final TwitchBot bot) {
        super(bot);
    }
}
