package fr.funixgaming.twitch.api.chatbot_irc.entities;

import fr.funixgaming.twitch.api.chatbot_irc.TagParser;
import lombok.Getter;

@Getter
public class ChatMember extends User {

    protected final UserBadges badges;
    protected final int roomID;
    protected final String channelName;

    public ChatMember(final TagParser parser) {
        super(parser);
        this.badges = new UserBadges(parser);
        this.roomID = Integer.parseInt(parser.getTagMap().get("room-id"));
        this.channelName = parser.getChannel();
    }
}
