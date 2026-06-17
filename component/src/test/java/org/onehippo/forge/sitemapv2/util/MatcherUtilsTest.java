package org.onehippo.forge.sitemapv2.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * JUnit 5 tests for {@link MatcherUtils}.
 */
class MatcherUtilsTest {

    @Test
    void getCommaSeparatedValues_returnsEmptyArrayForNullInput() {
        String[] result = MatcherUtils.getCommaSeparatedValues(null);
        assertNotNull(result);
        assertEquals(0, result.length);
    }

    @Test
    void getCommaSeparatedValues_returnsSingleElementForNoComma() {
        String[] result = MatcherUtils.getCommaSeparatedValues("single");
        assertArrayEquals(new String[]{"single"}, result);
    }

    @Test
    void getCommaSeparatedValues_splitsOnCommaWithVariousWhitespace() {
        String input = "  value1, \n\t value2  ,value3 , value4,value5";
        String[] result = MatcherUtils.getCommaSeparatedValues(input);
        assertArrayEquals(new String[]{"value1", "value2", "value3", "value4", "value5"}, result);
    }

    @Test
    void getCommaSeparatedValues_trimsLeadingAndTrailingWhitespaceFromInput() {
        String[] result = MatcherUtils.getCommaSeparatedValues("  alpha , beta  ");
        assertArrayEquals(new String[]{"alpha", "beta"}, result);
    }

    @Test
    void getCommaSeparatedValues_handlesEmptyString() {
        String[] result = MatcherUtils.getCommaSeparatedValues("");
        assertNotNull(result);
        // empty string splits to [""]
        assertEquals(1, result.length);
        assertEquals("", result[0]);
    }
}
