package org.onehippo.forge.sitemapv2.filter;

import java.util.function.Predicate;

import org.hippoecm.hst.configuration.sitemap.HstSiteMapItem;
import org.onehippo.forge.sitemapv2.api.HstSitemapItemFilter;

import static org.hippoecm.hst.configuration.HstNodeTypes.INDEX;

public class IsNotIndexFilter implements HstSitemapItemFilter {

    @Override
    public Predicate<HstSiteMapItem> filter() {
        return siteMapItem -> !siteMapItem.getValue().equals(INDEX);
    }
}
