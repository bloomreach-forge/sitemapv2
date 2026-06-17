package org.onehippo.forge.sitemapv2.components.model;

import org.junit.jupiter.api.Test;
import org.onehippo.forge.sitemapv2.components.model.ChangeFrequency;
import org.onehippo.forge.sitemapv2.components.model.Url;

import java.math.BigDecimal;
import java.util.Calendar;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * JUnit 5 tests for {@link Url}.
 */
class UrlTest {

    @Test
    void compareTo_ordersByLoc() {
        Url a = urlWithLoc("https://example.com/aaa");
        Url b = urlWithLoc("https://example.com/bbb");
        assertTrue(a.compareTo(b) < 0);
        assertTrue(b.compareTo(a) > 0);
        assertEquals(0, a.compareTo(urlWithLoc("https://example.com/aaa")));
    }

    @Test
    void equals_trueForSameLoc() {
        Url a = urlWithLoc("https://example.com/page");
        Url b = urlWithLoc("https://example.com/page");
        assertEquals(a, b);
    }

    @Test
    void equals_falseForDifferentLoc() {
        assertFalse(urlWithLoc("https://example.com/a").equals(urlWithLoc("https://example.com/b")));
    }

    @Test
    void hashCode_equalForSameLoc() {
        Url a = urlWithLoc("https://example.com/foo");
        Url b = urlWithLoc("https://example.com/foo");
        assertEquals(a.hashCode(), b.hashCode());
    }

    @Test
    void getChangeFrequencyAsString_returnsNullWhenNotSet() {
        Url url = new Url();
        url.setLoc("https://example.com");
        assertNull(url.getChangeFrequencyAsString());
    }

    @Test
    void getChangeFrequencyAsString_returnsDescription() {
        Url url = new Url();
        url.setLoc("https://example.com");
        url.setChangeFrequency(ChangeFrequency.WEEKLY);
        assertEquals("weekly", url.getChangeFrequencyAsString());
    }

    @Test
    void getlastModInW3CFormat_returnsNullWhenNotSet() {
        Url url = new Url();
        url.setLoc("https://example.com");
        assertNull(url.getlastModInW3CFormat());
    }

    @Test
    void getlastModInW3CFormat_returnsFormattedDate() {
        Url url = new Url();
        url.setLoc("https://example.com");
        Calendar cal = Calendar.getInstance();
        url.setLastmod(cal);
        assertNotNull(url.getlastModInW3CFormat());
        // ISO 8601 extended with time zone e.g. 2024-01-01T12:00:00+00:00
        assertTrue(url.getlastModInW3CFormat().contains("T"));
    }

    @Test
    void setLastmod_withNullStoredAsNull() {
        Url url = new Url();
        url.setLoc("https://example.com");
        url.setLastmod(null);
        assertNull(url.getlastModInW3CFormat());
    }

    @Test
    void priority_roundtrip() {
        Url url = new Url();
        url.setLoc("https://example.com");
        url.setPriority(new BigDecimal("0.8"));
        assertEquals(new BigDecimal("0.8"), url.getPriority());
    }

    // helpers

    private static Url urlWithLoc(String loc) {
        Url url = new Url();
        url.setLoc(loc);
        return url;
    }
}
