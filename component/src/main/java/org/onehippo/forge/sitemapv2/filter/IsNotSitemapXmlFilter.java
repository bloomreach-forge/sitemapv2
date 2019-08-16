package org.onehippo.forge.sitemapv2.filter;

import java.util.function.Predicate;

import org.hippoecm.hst.configuration.sitemap.HstSiteMapItem;
import org.onehippo.forge.sitemapv2.api.HstSitemapItemFilter;

public class IsNotSitemapXmlFilter implements HstSitemapItemFilter {

    @Override
    public Predicate<HstSiteMapItem> filter() {
        return siteMapItem -> !"sitemap.xml".equals(siteMapItem.getId());
    }
}
