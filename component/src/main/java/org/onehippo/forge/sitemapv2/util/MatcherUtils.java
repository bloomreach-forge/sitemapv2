package org.onehippo.forge.sitemapv2.util;

public class MatcherUtils {

    private static final String COMMA_SEPARATING_REG_EXP = "[\\s]*,[\\s]*";

    /**
     * Get the comma separated values from a string.
     */
    public static String[] getCommaSeparatedValues(final String values) {
        if (values == null) {
            return new String[]{};
        }
        return values.trim().split(COMMA_SEPARATING_REG_EXP);
    }

}
