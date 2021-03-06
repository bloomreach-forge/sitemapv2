package org.onehippo.forge.sitemapv2.components;

import java.util.Set;

import com.google.common.collect.Sets;

import org.hippoecm.hst.core.component.HstComponentException;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import org.hippoecm.hst.core.parameters.ParametersInfo;
import org.onehippo.forge.sitemapv2.api.SitemapEntriesBuilder;
import org.onehippo.forge.sitemapv2.builder.DefaultDocumentSitemapEntriesBuilder;
import org.onehippo.forge.sitemapv2.builder.DefaultSitemapEntriesBuilder;
import org.onehippo.forge.sitemapv2.info.DefaultSitemapFeedInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The default and most simplest sitemap feed. Used for Small websites and/or local development.
 * It will iterate through sitemap items and find corresponding pages (inside and outside of the workspace)
 * and it will search documents and will list all canonical URLs.
 */
@ParametersInfo(type = DefaultSitemapFeedInfo.class)
public class DefaultSitemapFeed extends AbstractSitemapFeed {

    //Builder for document
    private static final SitemapEntriesBuilder<DefaultSitemapFeedInfo> DOCUMENT_SITEMAP_BUILDER = new DefaultDocumentSitemapEntriesBuilder();
    //Builder for hst sitemap items inside and outside of workspace
    private static final SitemapEntriesBuilder<DefaultSitemapFeedInfo> HST_SITEMAP_SITEMAP_BUILDER = new DefaultSitemapEntriesBuilder();
    //collected list of builders and order
    private static final Set<SitemapEntriesBuilder> DEFAULT_BUILDERS = Sets.newHashSet(DOCUMENT_SITEMAP_BUILDER, HST_SITEMAP_SITEMAP_BUILDER);
    private static Logger log = LoggerFactory.getLogger(DefaultSitemapFeed.class);

    @SuppressWarnings("Duplicates")
    @Override
    public void doBeforeRender(final HstRequest request, final HstResponse response) throws HstComponentException {
        super.doBeforeRender(request, response);
    }

    protected Set<SitemapEntriesBuilder> getBuilders() {
        return DEFAULT_BUILDERS;
    }

}
