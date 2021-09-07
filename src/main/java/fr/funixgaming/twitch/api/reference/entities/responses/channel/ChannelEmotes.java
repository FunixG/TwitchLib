package fr.funixgaming.twitch.api.reference.entities.responses.channel;

import fr.funixgaming.twitch.api.chatbot_irc.parsers.NoticeEventParser;
import fr.funixgaming.twitch.api.reference.entities.TwitchApiEntity;
import fr.funixgaming.twitch.api.reference.entities.responses.TwitchImage;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ChannelEmotes extends TwitchApiEntity {
    public enum EmoteType {
        BITS,
        FOLLOWER,
        SUB;

        public static EmoteType getByType(final String type) {
            switch (type) {
                case "bitstier":
                    return BITS;
                case "follower":
                    return FOLLOWER;
                case "subscriptions":
                    return SUB;
                default:
                    return null;
            }
        }
    }

    private final String id;
    private final String name;
    private final TwitchImage images;
    private final NoticeEventParser.SubTier subTier;
    private final EmoteType emoteType;

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ChannelEmotes) {
            final ChannelEmotes channelEmotes = (ChannelEmotes) obj;
            return channelEmotes.getId().equals(id);
        } else {
            return false;
        }
    }
}
