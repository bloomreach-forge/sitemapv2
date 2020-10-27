package org.onehippo.forge.sitemapv2.components;

import java.util.Set;

import com.google.common.collect.Sets;

import org.hippoecm.hst.core.component.HstComponentException;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import org.hippoecm.hst.core.parameters.ParametersInfo;
import org.onehippo.forge.sitemapv2.api.SitemapBuilder;
import org.onehippo.forge.sitemapv2.api.SitemapGenerator;
import org.onehippo.forge.sitemapv2.builder.DefaultDocumentNewsSitemapBuilder;
import org.onehippo.forge.sitemapv2.generator.SitemapNewsGenerator;
import org.onehippo.forge.sitemapv2.info.DefaultNewsSitemapFeedInfo;

@ParametersInfo(type = DefaultNewsSitemapFeedInfo.class)
public class NewsFeed extends AbstractSitemapFeed {

    //Builder for document
    private static final SitemapBuilder DOCUMENT_SITEMAP_BUILDER = new DefaultDocumentNewsSitemapBuilder();

    @Override
    public void doBeforeRender(final HstRequest request, final HstResponse response) throws HstComponentException {
        super.doBeforeRender(request, response);
    }

    @Override
    protected Set<SitemapBuilder> getBuilders() {
        return Sets.newHashSet(DOCUMENT_SITEMAP_BUILDER);
    }

    protected SitemapGenerator getSitemapGenerator() {
        return new SitemapNewsGenerator();
    }

}
