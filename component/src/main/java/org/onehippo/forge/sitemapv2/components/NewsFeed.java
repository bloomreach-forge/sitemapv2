package org.onehippo.forge.sitemapv2.components;

import java.util.Set;

import com.google.common.collect.Sets;

import org.hippoecm.hst.core.component.HstComponentException;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import org.hippoecm.hst.core.parameters.ParametersInfo;
import org.onehippo.forge.sitemapv2.api.SitemapEntriesBuilder;
import org.onehippo.forge.sitemapv2.api.SitemapGenerator;
import org.onehippo.forge.sitemapv2.builder.DefaultDocumentNewsSitemapEntriesBuilder;
import org.onehippo.forge.sitemapv2.generator.SitemapNewsGenerator;
import org.onehippo.forge.sitemapv2.info.DefaultNewsSitemapFeedInfo;

@ParametersInfo(type = DefaultNewsSitemapFeedInfo.class)
public class NewsFeed extends AbstractSitemapFeed {

    //Builder for document
    private static final SitemapEntriesBuilder DOCUMENT_SITEMAP_BUILDER = new DefaultDocumentNewsSitemapEntriesBuilder();

    @Override
    public void doBeforeRender(final HstRequest request, final HstResponse response) throws HstComponentException {
        super.doBeforeRender(request, response);
    }

    @Override
    protected Set<SitemapEntriesBuilder> getBuilders() {
        return Sets.newHashSet(DOCUMENT_SITEMAP_BUILDER);
    }

    protected SitemapGenerator getSitemapGenerator() {
        return new SitemapNewsGenerator();
    }

}
