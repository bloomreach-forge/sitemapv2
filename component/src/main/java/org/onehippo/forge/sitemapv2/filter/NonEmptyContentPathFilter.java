package org.onehippo.forge.sitemapv2.filter;

import java.util.function.Predicate;


import com.google.common.base.Strings;
import org.hippoecm.hst.configuration.sitemap.HstSiteMapItem;
import org.onehippo.forge.sitemapv2.api.HstSitemapItemFilter;

public class NonEmptyContentPathFilter implements HstSitemapItemFilter {

    @Override
    public Predicate<HstSiteMapItem> filter() {
        return siteMapItem -> Strings.isNullOrEmpty(siteMapItem.getRelativeContentPath());
    }
}
