/*
 * Copyright 2026 Bloomreach B.V. (https://www.bloomreach.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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