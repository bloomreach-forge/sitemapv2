package org.onehippo.forge.sitemapv2.components;

import java.util.Set;

import com.google.common.collect.Sets;

import org.hippoecm.hst.component.support.bean.BaseHstComponent;
import org.hippoecm.hst.core.component.HstComponentException;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import org.hippoecm.hst.core.parameters.ParametersInfo;
import org.onehippo.forge.sitemapv2.api.SitemapBuilder;
import org.onehippo.forge.sitemapv2.builder.DefaultDocumentSitemapBuilder;
import org.onehippo.forge.sitemapv2.builder.DefaultHstSitemapSitemapBuilder;
import org.onehippo.forge.sitemapv2.generator.SitemapGenerator;
import org.onehippo.forge.sitemapv2.info.DefaultSitemapFeedInfo;

@ParametersInfo(type = DefaultSitemapFeedInfo.class)
public class DefaultSitemapFeed extends BaseHstComponent {

    //Builder for document
    private static final SitemapBuilder<DefaultSitemapFeedInfo> DOCUMENT_SITEMAP_BUILDER = new DefaultDocumentSitemapBuilder();
    //Builder for hst sitemap items inside and outside of workspace
    private static final SitemapBuilder<DefaultSitemapFeedInfo> HST_SITEMAP_SITEMAP_BUILDER = new DefaultHstSitemapSitemapBuilder();
    //collected list of builders and order
    private static final Set<SitemapBuilder> DEFAULT_BUILDERS = Sets.newHashSet(DOCUMENT_SITEMAP_BUILDER, HST_SITEMAP_SITEMAP_BUILDER);

    @Override
    public void doBeforeRender(final HstRequest request, final HstResponse response) throws HstComponentException {
        super.doBeforeRender(request, response);
        long startTime = System.nanoTime();
        String siteMap = getSiteMap(request);
        long endTime = System.nanoTime();
        long duration = (endTime - startTime);
        System.out.println("ms:" + duration / 1000000);
        System.out.println("mb:" + siteMap.length() / 1000000f);
        request.setAttribute("sitemap", siteMap);
    }


    /**
     * Get site map from cache or generate the site map from HST
     */
    protected String getSiteMap(final HstRequest request) {
        return buildSiteMap(request);
    }

    /**
     * Builds the sitemap and passes it to the SitemapGenerator
     * available for override in sub class
     */
    protected String buildSiteMap(final HstRequest request) {
        final SitemapGenerator generator = new SitemapGenerator();

        DefaultSitemapFeedInfo info = getComponentParametersInfo(request);

        getBuilders().forEach(sitemapBuilder -> sitemapBuilder.build(request, info, generator));

        System.out.println("size:" + generator.getSize());

        return generator.getSitemap();
    }

    /**
     * available for override in sub class to add or replace default builders
     *
     * @return
     */
    protected Set<SitemapBuilder> getBuilders() {
        return DEFAULT_BUILDERS;
    }

}
