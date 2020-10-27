package org.onehippo.forge.sitemapv2.api;

import org.hippoecm.hst.core.component.HstRequest;
import org.onehippo.forge.sitemapv2.builder.DefaultDocumentSitemapEntriesBuilder;
import org.onehippo.forge.sitemapv2.builder.DefaultSitemapEntriesBuilder;

public interface SitemapEntriesBuilder<T> {

    /***
     * Build the sitemap.xml according to implementation.
     * This could either be {@link DefaultDocumentSitemapEntriesBuilder} explicitly for documents
     * or {@link DefaultSitemapEntriesBuilder} explicitly for landing pages
     * @param request
     * @param componentInfo
     * @param generator
     */
    void build(final HstRequest request, final T componentInfo, final SitemapGenerator generator);

}
