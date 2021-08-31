package fr.funixgaming.twitch.api.chatbot_irc.parsers;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;

@Getter
public class NoticeEventParser {

    @Getter(AccessLevel.PRIVATE)
    @AllArgsConstructor
    public enum NoticeType {
        SUB("sub"),
        RESUB("resub"),
        SUB_GIFT("subgift"),
        SUBGIFT_ANONYM("anonsubgift"),
        RAID("raid");

        private final String twitchTag;
    }

    @Getter(AccessLevel.PRIVATE)
    @AllArgsConstructor
    public enum SubTier {
        PRIME("Prime"),
        TIER_1("1000"),
        TIER_2("2000"),
        TIER_3("3000");

        private final String twitchTag;
    }

    private NoticeType noticeType;
    private SubTier subTier;
    private final String subGiftReceiverUsername;
    private final String subGiftReceiverId;
    private final Integer months;
    private final String roomId;
    private final String raiderUsername;
    private final Integer raidViewerCount;
    private final String channel;

    public NoticeEventParser(final TagParser parser) {
        final Map<String, String> tagMap = parser.getTagMap();

        parseNoticeType(tagMap);
        parseSubTierType(tagMap);
        this.channel = parser.getChannel();
        this.raiderUsername = tagMap.get("msg-param-displayName");
        this.subGiftReceiverUsername = tagMap.get("msg-param-recipient-display-name");
        this.subGiftReceiverId = tagMap.get("msg-param-recipient-id");
        this.roomId = tagMap.get("room-id");

        final String nbrMonths = tagMap.get("msg-param-months");
        final String raidCount = tagMap.get("msg-param-viewerCount");
        this.months = nbrMonths == null ? null : Integer.parseInt(nbrMonths);
        this.raidViewerCount = raidCount == null ? null : Integer.parseInt(raidCount);
    }

    private void parseNoticeType(final Map<String, String> tagMap) {
        final String msgNoticeType = tagMap.get("msg-id");

        if (msgNoticeType != null) {
            for (final NoticeType noticeTypeGet : NoticeType.values()) {
                if (noticeTypeGet.getTwitchTag().equals(msgNoticeType)) {
                    this.noticeType = noticeTypeGet;
                    return;
                }
            }
        }
    }

    private void parseSubTierType(final Map<String, String> tagMap) {
        final String subPlan = tagMap.get("msg-param-sub-plan");

        if (subPlan != null) {
            for (final SubTier subPlanGet : SubTier.values()) {
                if (subPlanGet.getTwitchTag().equals(subPlan)) {
                    this.subTier = subPlanGet;
                    return;
                }
            }
        }
    }

}
