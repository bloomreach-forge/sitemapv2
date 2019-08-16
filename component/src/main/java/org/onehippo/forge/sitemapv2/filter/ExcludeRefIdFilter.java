package org.onehippo.forge.sitemapv2.filter;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

import org.hippoecm.hst.configuration.sitemap.HstSiteMapItem;
import org.onehippo.forge.sitemapv2.api.HstSitemapItemFilter;

public class ExcludeRefIdFilter implements HstSitemapItemFilter {

    private final List<String> refIdList;

    public ExcludeRefIdFilter(String... refids) {
        this.refIdList = Arrays.asList(refids);
    }

    @Override
    public Predicate<HstSiteMapItem> filter() {
        return siteMapItem -> !this.refIdList.contains(siteMapItem.getRefId());
    }
}
