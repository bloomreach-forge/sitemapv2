package org.onehippo.forge.sitemap.components.model.util;

import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.junit.Test;
import org.onehippo.forge.sitemapv2.components.model.ChangeFrequency;
import org.onehippo.forge.sitemapv2.util.MatcherUtils;

import static org.junit.Assert.assertTrue;

public class UtilsTest {

    @Test
    public void getChangeFrequencyFromDescription() {
        assertTrue(ChangeFrequency.valueOf("DAILY").equals(ChangeFrequency.DAILY));

    }

    @Test
    public void testGetCommaSeparatedValues() throws Exception {

        final String input = "  value1, \n\t value2  ,value3 , value4,value5";
        final String[] expected = new String[]{"value1", "value2", "value3", "value4", "value5"};
        final String[] actual = MatcherUtils.getCommaSeparatedValues(input);

        Assert.assertEquals("Lengths of resulting arrays don't match", 5, actual.length);
        Assert.assertEquals(expected[0], actual[0]);
        Assert.assertEquals(expected[1], actual[1]);
        Assert.assertEquals(expected[2], actual[2]);
        Assert.assertEquals(expected[3], actual[3]);
        Assert.assertEquals(expected[4], actual[4]);

    }


    @Test
    public void testTimeUnit() {
        TimeUnit seconds = TimeUnit.valueOf("SECONDS");
        assertTrue(seconds.equals(TimeUnit.SECONDS));
    }
}