package me.oondanomala.essential;

public final class TrustedHostsUtil {
    // No point in overcomplicating it when this is all we need
    private static final String[] TRUSTED_HOSTS = {
        "cdn.discordapp.com", "media.discordapp.net",
        "essential-artifacts-1.nyc3.cdn.digitaloceanspaces.com", "cdn.essential.gg", "essential.gg",
        "media.essential.gg",
        "i.redd.it",
        "pbs.twimg.com",
        "i.badlion.net",
        "i.imgur.com", "imgur.com"
    };

    private TrustedHostsUtil() {
    }

    public static String[] getTrustedHosts() {
        return TRUSTED_HOSTS;
    }
}
