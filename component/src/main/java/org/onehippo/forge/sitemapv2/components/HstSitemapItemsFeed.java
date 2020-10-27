package org.onehippo.forge.sitemapv2.components;

import java.util.Set;

import com.google.common.collect.Sets;

import org.hippoecm.hst.core.component.HstComponentException;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import org.hippoecm.hst.core.parameters.ParametersInfo;
import org.onehippo.forge.sitemapv2.api.SitemapBuilder;
import org.onehippo.forge.sitemapv2.builder.DefaultSitemapBuilder;
import org.onehippo.forge.sitemapv2.info.DefaultSitemapFeedInfo;

@ParametersInfo(type = DefaultSitemapFeedInfo.class)
public class HstSitemapItemsFeed extends AbstractSitemapFeed {

    //Builder for hst sitemap items inside and outside of workspace
    private static final SitemapBuilder<DefaultSitemapFeedInfo> HST_SITEMAP_SITEMAP_BUILDER = new DefaultSitemapBuilder();
    private static final Set<SitemapBuilder> HST_SITEMAP_BUILDER = Sets.newHashSet(HST_SITEMAP_SITEMAP_BUILDER);

    @SuppressWarnings("Duplicates")
    @Override
    public void doBeforeRender(final HstRequest request, final HstResponse response) throws HstComponentException {
        super.doBeforeRender(request, response);
    }

    /**
     * available for override in sub class to add or replace default builders
     *
     * @return
     */
    protected Set<SitemapBuilder> getBuilders() {
        return HST_SITEMAP_BUILDER;
    }
}
