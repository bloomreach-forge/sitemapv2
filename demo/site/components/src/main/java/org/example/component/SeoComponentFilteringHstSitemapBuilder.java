package org.example.component;

import java.util.Set;

import org.onehippo.forge.sitemapv2.api.HstSitemapItemFilter;
import org.onehippo.forge.sitemapv2.builder.DefaultHstSitemapSitemapBuilder;

public class SeoComponentFilteringHstSitemapBuilder extends DefaultHstSitemapSitemapBuilder {

    private static final HstSitemapItemFilter SEO_COMPONENT_INDEX_FILTER = new SeoComponentIndexFilter();

    @Override
    protected Set<HstSitemapItemFilter> getFilters() {
        Set<HstSitemapItemFilter> filters = super.getFilters();
        filters.add(SEO_COMPONENT_INDEX_FILTER);
        return filters;
    }
}
