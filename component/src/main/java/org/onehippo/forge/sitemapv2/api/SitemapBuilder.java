package org.onehippo.forge.sitemapv2.api;

import org.hippoecm.hst.core.component.HstRequest;
import org.onehippo.forge.sitemapv2.generator.SitemapGenerator;

public interface SitemapBuilder<T> {

    /***
     * Build the sitemap.xml according to implementation.
     * This could either be {@link org.onehippo.forge.sitemapv2.builder.DefaultDocumentSitemapBuilder} explicitly for documents
     * or {@link org.onehippo.forge.sitemapv2.builder.DefaultSitemapBuilder} explicitly for landing pages
     * @param request
     * @param componentInfo
     * @param generator
     */
    void build(final HstRequest request, final T componentInfo, final SitemapGenerator generator);

}
