package org.example.component;

import java.util.Set;
import java.util.stream.Collectors;

import org.hippoecm.hst.core.parameters.ParametersInfo;
import org.onehippo.forge.sitemapv2.api.SitemapBuilder;
import org.onehippo.forge.sitemapv2.builder.DefaultHstSitemapSitemapBuilder;
import org.onehippo.forge.sitemapv2.components.HstSitemapItemsFeed;
import org.onehippo.forge.sitemapv2.info.DefaultSitemapFeedInfo;

@ParametersInfo(type = DefaultSitemapFeedInfo.class)
public class ExtendedHstSitemapItemsFeed extends HstSitemapItemsFeed {

    private static final SeoComponentFilteringHstSitemapBuilder SEO_COMPONENT_FILTERING_HST_SITEMAP_BUILDER = new SeoComponentFilteringHstSitemapBuilder();

    @Override
    protected Set<SitemapBuilder> getBuilders() {
        //replacing the default
        Set<SitemapBuilder> builders = super.getBuilders();
        builders = builders.stream()
                .map(sitemapBuilder -> sitemapBuilder instanceof DefaultHstSitemapSitemapBuilder ? SEO_COMPONENT_FILTERING_HST_SITEMAP_BUILDER : sitemapBuilder)
                .collect(Collectors.toSet());
        return builders;
    }
}
