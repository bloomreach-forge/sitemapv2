package org.onehippo.forge.sitemapv2.components;

import java.util.Set;

import com.google.common.collect.Sets;

import org.hippoecm.hst.core.component.HstComponentException;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import org.hippoecm.hst.core.parameters.ParametersInfo;
import org.onehippo.forge.sitemapv2.api.SitemapEntriesBuilder;
import org.onehippo.forge.sitemapv2.builder.DefaultDocumentSitemapEntriesBuilder;
import org.onehippo.forge.sitemapv2.info.DefaultSitemapFeedInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ParametersInfo(type = DefaultSitemapFeedInfo.class)
public class DocumentFeed extends AbstractSitemapFeed {

    //Builder for document
    private static final SitemapEntriesBuilder<DefaultSitemapFeedInfo> DOCUMENT_SITEMAP_BUILDER = new DefaultDocumentSitemapEntriesBuilder();
    private static final Logger LOG = LoggerFactory.getLogger(DocumentFeed.class);

    @Override
    public void doBeforeRender(final HstRequest request, final HstResponse response) throws HstComponentException {
        super.doBeforeRender(request, response);
    }

    @Override
    protected Set<SitemapEntriesBuilder> getBuilders() {
        return Sets.newHashSet(DOCUMENT_SITEMAP_BUILDER);
    }
}
