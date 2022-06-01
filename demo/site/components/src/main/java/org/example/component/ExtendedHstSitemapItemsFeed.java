package org.example.component;

import java.util.Set;
import java.util.stream.Collectors;

import org.hippoecm.hst.core.parameters.ParametersInfo;
import org.onehippo.forge.sitemapv2.api.SitemapEntriesBuilder;
import org.onehippo.forge.sitemapv2.builder.DefaultSitemapEntriesBuilder;
import org.onehippo.forge.sitemapv2.components.HstSitemapItemsFeed;
import org.onehippo.forge.sitemapv2.info.DefaultSitemapFeedInfo;

@ParametersInfo(type = DefaultSitemapFeedInfo.class)
public class ExtendedHstSitemapItemsFeed extends HstSitemapItemsFeed {

    private static final SeoComponentFilteringHstSitemapEntriesBuilder SEO_COMPONENT_FILTERING_HST_SITEMAP_BUILDER = new SeoComponentFilteringHstSitemapEntriesBuilder();

    @Override
    protected Set<SitemapEntriesBuilder> getBuilders() {
        //replacing the default
        Set<SitemapEntriesBuilder> builders = super.getBuilders();
        builders = builders.stream()
                .map(sitemapBuilder -> sitemapBuilder instanceof DefaultSitemapEntriesBuilder ? SEO_COMPONENT_FILTERING_HST_SITEMAP_BUILDER : sitemapBuilder)
                .collect(Collectors.toSet());
        return builders;
    }
}
