package org.onehippo.forge.sitemapv2.api;

import java.util.function.Predicate;

import org.hippoecm.hst.configuration.sitemap.HstSiteMapItem;

public interface HstSitemapItemFilter {

    /***
     * Filters out the list of sitemap items. Such as wildcard items and container resources
     * @return
     */
    Predicate<HstSiteMapItem> filter();
}
