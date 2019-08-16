package org.onehippo.forge.sitemap.components.model;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.easymock.EasyMock;
import org.hippoecm.hst.configuration.sitemap.HstSiteMapItem;
import org.onehippo.forge.sitemapv2.api.HstSitemapItemFilter;
import org.onehippo.forge.sitemapv2.filter.IsNotContainerResourceFilter;
import org.onehippo.forge.sitemapv2.filter.NonEmptyContentPathFilter;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.junit.Assert.assertTrue;

public class TestCompositePredicatesTest {

    @org.junit.Test
    public void testPredicates() {

        HstSitemapItemFilter nonEmptyContentPathFilter = new NonEmptyContentPathFilter();
        HstSitemapItemFilter isNotContainerResourceFilter = new IsNotContainerResourceFilter();

        List<Predicate<HstSiteMapItem>> allPredicates = Arrays.asList(nonEmptyContentPathFilter.filter(), isNotContainerResourceFilter.filter());

        Predicate<HstSiteMapItem> compositePredicate =
                allPredicates.stream()
                        .reduce(w -> true, Predicate::and);

        HstSiteMapItem item1 = EasyMock.createNiceMock(HstSiteMapItem.class);
        HstSiteMapItem item2 = EasyMock.createNiceMock(HstSiteMapItem.class);
        HstSiteMapItem item3 = EasyMock.createNiceMock(HstSiteMapItem.class);
        HstSiteMapItem item4 = EasyMock.createNiceMock(HstSiteMapItem.class);

        EasyMock.expect(item1.getRelativeContentPath()).andReturn("notempty").anyTimes();
        EasyMock.expect(item1.isContainerResource()).andReturn(false).anyTimes();
        EasyMock.expect(item2.getRelativeContentPath()).andReturn("").anyTimes();
        EasyMock.expect(item2.isContainerResource()).andReturn(false).anyTimes();
        EasyMock.expect(item3.isContainerResource()).andReturn(true).anyTimes();
        EasyMock.expect(item3.getRelativeContentPath()).andReturn("/foo/bar1").anyTimes();
        EasyMock.expect(item4.isContainerResource()).andReturn(false).anyTimes();
        EasyMock.expect(item4.getRelativeContentPath()).andReturn("/foo/bar2").anyTimes();

        EasyMock.replay(item1, item2, item3, item4);


        List<HstSiteMapItem> collect = Arrays.asList(item1, item2, item3, item4).stream()
                .filter(compositePredicate)
                .collect(Collectors.toList());

        assertTrue(collect.size() == 1);


    }
}