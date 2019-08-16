package org.onehippo.forge.sitemapv2.api;

import java.util.function.Predicate;

import org.hippoecm.hst.configuration.sitemap.HstSiteMapItem;

public interface HstSitemapItemFilter {

    Predicate<HstSiteMapItem> filter();
}
