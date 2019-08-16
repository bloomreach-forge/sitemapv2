package org.onehippo.forge.sitemapv2.filter;

import java.util.function.Predicate;

import org.hippoecm.hst.configuration.sitemap.HstSiteMapItem;
import org.onehippo.forge.sitemapv2.api.HstSitemapItemFilter;

public class IsHiddenInChannelManagerFilter implements HstSitemapItemFilter {

    @Override
    public Predicate<HstSiteMapItem> filter() {
        return siteMapItem -> !siteMapItem.isHiddenInChannelManager();
    }
}
