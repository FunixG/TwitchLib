package fr.funixgaming.twitch.api.chatbot_irc.entities;

import fr.funixgaming.twitch.api.chatbot_irc.parsers.TagParser;
import lombok.Getter;

@Getter
public class ChatMember extends User {

    protected final UserBadges badges;
    protected final String roomID;
    protected final String channelName;

    public ChatMember(final TagParser parser) {
        super(parser);
        this.badges = new UserBadges(parser);
        this.roomID = parser.getTagMap().get("room-id");
        this.channelName = parser.getChannel();
    }
}
