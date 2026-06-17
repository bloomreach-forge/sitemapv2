package org.onehippo.forge.sitemapv2.filter;

import org.hippoecm.hst.configuration.sitemap.HstSiteMapItem;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

/**
 * JUnit 5 tests for all HstSitemapItemFilter implementations.
 * No live JCR/Spring context required — all HST interactions are mocked.
 */
@ExtendWith(MockitoExtension.class)
class HstSitemapItemFiltersTest {

    @Mock
    private HstSiteMapItem item;

    // ---------------------------------------------------------------
    // DoesNotContainAnyFilter
    // ---------------------------------------------------------------

    @Test
    void doesNotContainAny_returnsTrueWhenItemDoesNotContainAny() {
        when(item.containsAny()).thenReturn(false);
        Predicate<HstSiteMapItem> predicate = new DoesNotContainAnyFilter().filter();
        assertTrue(predicate.test(item));
    }

    @Test
    void doesNotContainAny_returnsFalseWhenItemContainsAny() {
        when(item.containsAny()).thenReturn(true);
        Predicate<HstSiteMapItem> predicate = new DoesNotContainAnyFilter().filter();
        assertFalse(predicate.test(item));
    }

    // ---------------------------------------------------------------
    // DoesNotContainDefaultFilter (containsWildCard maps to "default")
    // ---------------------------------------------------------------

    @Test
    void doesNotContainDefault_returnsTrueWhenNoWildCard() {
        when(item.containsWildCard()).thenReturn(false);
        Predicate<HstSiteMapItem> predicate = new DoesNotContainDefaultFilter().filter();
        assertTrue(predicate.test(item));
    }

    @Test
    void doesNotContainDefault_returnsFalseWhenContainsWildCard() {
        when(item.containsWildCard()).thenReturn(true);
        Predicate<HstSiteMapItem> predicate = new DoesNotContainDefaultFilter().filter();
        assertFalse(predicate.test(item));
    }

    // ---------------------------------------------------------------
    // ExcludeRefIdFilter
    // ---------------------------------------------------------------

    @Test
    void excludeRefId_returnsFalseForMatchingRefId() {
        when(item.getRefId()).thenReturn("excluded-id");
        Predicate<HstSiteMapItem> predicate = new ExcludeRefIdFilter("excluded-id", "other-id").filter();
        assertFalse(predicate.test(item));
    }

    @Test
    void excludeRefId_returnsTrueForNonMatchingRefId() {
        when(item.getRefId()).thenReturn("allowed-id");
        Predicate<HstSiteMapItem> predicate = new ExcludeRefIdFilter("excluded-id", "other-id").filter();
        assertTrue(predicate.test(item));
    }

    @Test
    void excludeRefId_returnsTrueWhenExclusionListIsEmpty() {
        when(item.getRefId()).thenReturn("any-id");
        Predicate<HstSiteMapItem> predicate = new ExcludeRefIdFilter().filter();
        assertTrue(predicate.test(item));
    }

    // ---------------------------------------------------------------
    // IsHiddenInChannelManagerFilter
    // ---------------------------------------------------------------

    @Test
    void isHiddenInChannelManager_returnsFalseWhenHidden() {
        when(item.isHiddenInChannelManager()).thenReturn(true);
        Predicate<HstSiteMapItem> predicate = new IsHiddenInChannelManagerFilter().filter();
        assertFalse(predicate.test(item));
    }

    @Test
    void isHiddenInChannelManager_returnsTrueWhenNotHidden() {
        when(item.isHiddenInChannelManager()).thenReturn(false);
        Predicate<HstSiteMapItem> predicate = new IsHiddenInChannelManagerFilter().filter();
        assertTrue(predicate.test(item));
    }

    // ---------------------------------------------------------------
    // IsNotAnyFilter
    // ---------------------------------------------------------------

    @Test
    void isNotAny_returnsTrueWhenNotAny() {
        when(item.isAny()).thenReturn(false);
        Predicate<HstSiteMapItem> predicate = new IsNotAnyFilter().filter();
        assertTrue(predicate.test(item));
    }

    @Test
    void isNotAny_returnsFalseWhenIsAny() {
        when(item.isAny()).thenReturn(true);
        Predicate<HstSiteMapItem> predicate = new IsNotAnyFilter().filter();
        assertFalse(predicate.test(item));
    }

    // ---------------------------------------------------------------
    // IsNotContainerResourceFilter
    // ---------------------------------------------------------------

    @Test
    void isNotContainerResource_returnsTrueWhenNotContainerResource() {
        when(item.isContainerResource()).thenReturn(false);
        Predicate<HstSiteMapItem> predicate = new IsNotContainerResourceFilter().filter();
        assertTrue(predicate.test(item));
    }

    @Test
    void isNotContainerResource_returnsFalseWhenContainerResource() {
        when(item.isContainerResource()).thenReturn(true);
        Predicate<HstSiteMapItem> predicate = new IsNotContainerResourceFilter().filter();
        assertFalse(predicate.test(item));
    }

    // ---------------------------------------------------------------
    // IsNotRobotsTxtFilter
    // ---------------------------------------------------------------

    @Test
    void isNotRobotsTxt_returnsTrueForNonRobotsId() {
        when(item.getId()).thenReturn("sitemap.xml");
        Predicate<HstSiteMapItem> predicate = new IsNotRobotsTxtFilter().filter();
        assertTrue(predicate.test(item));
    }

    @Test
    void isNotRobotsTxt_returnsFalseForRobotsId() {
        when(item.getId()).thenReturn("robots.txt");
        Predicate<HstSiteMapItem> predicate = new IsNotRobotsTxtFilter().filter();
        assertFalse(predicate.test(item));
    }

    // ---------------------------------------------------------------
    // IsNotSitemapXmlFilter
    // ---------------------------------------------------------------

    @Test
    void isNotSitemapXml_returnsTrueForNonSitemapId() {
        when(item.getId()).thenReturn("about-us");
        Predicate<HstSiteMapItem> predicate = new IsNotSitemapXmlFilter().filter();
        assertTrue(predicate.test(item));
    }

    @Test
    void isNotSitemapXml_returnsFalseForSitemapXmlId() {
        when(item.getId()).thenReturn("sitemap.xml");
        Predicate<HstSiteMapItem> predicate = new IsNotSitemapXmlFilter().filter();
        assertFalse(predicate.test(item));
    }

    @Test
    void isNotSitemapXml_returnsFalseForSitemapNewsXmlId() {
        when(item.getId()).thenReturn("sitemap-news.xml");
        Predicate<HstSiteMapItem> predicate = new IsNotSitemapXmlFilter().filter();
        assertFalse(predicate.test(item));
    }

    @Test
    void isNotSitemapXml_returnsTrueWhenStartsWithSitemapButNotXmlSuffix() {
        when(item.getId()).thenReturn("sitemap-index");
        Predicate<HstSiteMapItem> predicate = new IsNotSitemapXmlFilter().filter();
        assertTrue(predicate.test(item));
    }

    // ---------------------------------------------------------------
    // IsNotWildCardFilter
    // ---------------------------------------------------------------

    @Test
    void isNotWildCard_returnsTrueWhenNotWildCard() {
        when(item.isWildCard()).thenReturn(false);
        Predicate<HstSiteMapItem> predicate = new IsNotWildCardFilter().filter();
        assertTrue(predicate.test(item));
    }

    @Test
    void isNotWildCard_returnsFalseWhenWildCard() {
        when(item.isWildCard()).thenReturn(true);
        Predicate<HstSiteMapItem> predicate = new IsNotWildCardFilter().filter();
        assertFalse(predicate.test(item));
    }

    // ---------------------------------------------------------------
    // NonEmptyContentPathFilter
    // NOTE: the implementation returns true when the path IS null/empty
    // (i.e. the predicate keeps items whose content path is empty).
    // ---------------------------------------------------------------

    @Test
    void nonEmptyContentPath_returnsTrueForNullPath() {
        when(item.getRelativeContentPath()).thenReturn(null);
        Predicate<HstSiteMapItem> predicate = new NonEmptyContentPathFilter().filter();
        assertTrue(predicate.test(item));
    }

    @Test
    void nonEmptyContentPath_returnsTrueForEmptyPath() {
        when(item.getRelativeContentPath()).thenReturn("");
        Predicate<HstSiteMapItem> predicate = new NonEmptyContentPathFilter().filter();
        assertTrue(predicate.test(item));
    }

    @Test
    void nonEmptyContentPath_returnsFalseForNonEmptyPath() {
        when(item.getRelativeContentPath()).thenReturn("/content/documents/foo");
        Predicate<HstSiteMapItem> predicate = new NonEmptyContentPathFilter().filter();
        assertFalse(predicate.test(item));
    }
}
