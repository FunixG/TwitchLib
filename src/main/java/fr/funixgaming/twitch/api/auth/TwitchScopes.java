package fr.funixgaming.twitch.api.auth;

/**
 * Class used to know the twitch permissions
 * You choose the permission you want for the Twitch usage
 *
 * Not recomanded to set all permissions if you do not need it, in case if your key leaks
 */
public class TwitchScopes {
    public static final String ANALYTICS_READ_EXTENSIONS = "analytics:read:extensions";
    public static final String ANALYTICS_READ_GAMES = "analytics:read:games";
    public static final String BITS_READ = "bits:read";
    public static final String CHAT_READ = "chat:read";
    public static final String CHAT_EDIT = "chat:edit";
    public static final String CHANNEL_EDIT_COMMERCIAL = "channel:edit:commercial";
    public static final String CHANNEL_MODERATE = "channel:moderate";
    public static final String CHANNEL_EDITOR = "channel:editor";
    public static final String CHANNEL_MANAGE_BROADCAST = "channel:manage:broadcast";
    public static final String CHANNEL_MANAGE_EXTENSIONS = "channel:manage:extensions";
    public static final String CHANNEL_MANAGE_POLLS = "channel:manage:polls";
    public static final String CHANNEL_MANAGE_PREDICTIONS = "channel:manage:predictions";
    public static final String CHANNEL_MANAGE_CHAT_POINTS_REWARDS = "channel:manage:redemptions";
    public static final String CHANNEL_MANAGE_SCHEDULE = "channel:manage:schedule";
    public static final String CHANNEL_MANAGE_VIDEOS = "channel:manage:videos";
    public static final String CHANNEL_READ_EDITORS = "channel:read:editors";
    public static final String CHANNEL_READ_HYPE_TRAIN = "channel:read:hype_train";
    public static final String CHANNEL_READ_POLLS = "channel:read:polls";
    public static final String CHANNEL_READ_PREDICTIONS = "channel:read:predictions";
    public static final String CHANNEL_READ_CHAT_POINTS_REWARDS = "channel:read:redemptions";
    public static final String CHANNEL_READ_STREAM_KEY = "channel:read:stream_key";
    public static final String CHANNEL_READ_SUBS = "channel:read:subscriptions";
    public static final String EDIT_CLIPS = "clips:edit";
    public static final String MODERATION_READ = "moderation:read";
    public static final String MODERATION_MANAGE_AUTOMOD = "moderator:manage:automod";
    public static final String USER_EDIT = "user:edit";
    public static final String USER_EDIT_FOLLOWS = "user:edit:follows";
    public static final String USER_MANAGE_BLOCKED_USER = "user:manage:blocked_users";
    public static final String USER_READ_BLOCKED_USERS = "user:read:blocked_users";
    public static final String USER_READ_BROADCAST = "user:read:broadcast";
    public static final String USER_READ_EMAIL = "user:read:email";
    public static final String USER_READ_FOLLOWS = "user:read:follows";
    public static final String USER_READ_SUBS = "user:read:subscriptions";
    public static final String WHISPER = "whispers:edit";
    public static final String WHISPER_READ = "whispers:read";
}
