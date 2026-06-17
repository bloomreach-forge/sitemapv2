package org.onehippo.forge.sitemapv2.components.model;

import org.junit.jupiter.api.Test;
import org.onehippo.forge.sitemapv2.components.model.ChangeFrequency;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * JUnit 5 tests for {@link ChangeFrequency}.
 */
class ChangeFrequencyTest {

    @Test
    void toString_returnsLowercaseDescription() {
        assertEquals("always",  ChangeFrequency.ALWAYS.toString());
        assertEquals("hourly",  ChangeFrequency.HOURLY.toString());
        assertEquals("daily",   ChangeFrequency.DAILY.toString());
        assertEquals("weekly",  ChangeFrequency.WEEKLY.toString());
        assertEquals("monthly", ChangeFrequency.MONTHLY.toString());
        assertEquals("yearly",  ChangeFrequency.YEARLY.toString());
        assertEquals("never",   ChangeFrequency.NEVER.toString());
    }

    @Test
    void valueOf_parsesEnumByName() {
        assertEquals(ChangeFrequency.DAILY, ChangeFrequency.valueOf("DAILY"));
        assertEquals(ChangeFrequency.NEVER, ChangeFrequency.valueOf("NEVER"));
    }

    @Test
    void values_containsAllSevenEntries() {
        assertEquals(7, ChangeFrequency.values().length);
    }
}
