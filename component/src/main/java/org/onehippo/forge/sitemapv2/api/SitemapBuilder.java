package org.onehippo.forge.sitemapv2.api;

import org.hippoecm.hst.core.component.HstRequest;
import org.onehippo.forge.sitemapv2.generator.SitemapGenerator;

public interface SitemapBuilder<T> {

    void build(final HstRequest request, final T componentInfo, final SitemapGenerator generator);

}
