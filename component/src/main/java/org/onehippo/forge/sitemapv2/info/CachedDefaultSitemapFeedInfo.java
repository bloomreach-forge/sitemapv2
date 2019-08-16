package org.onehippo.forge.sitemapv2.info;

import org.hippoecm.hst.core.parameters.Parameter;

public interface CachedDefaultSitemapFeedInfo extends DefaultSitemapFeedInfo {

    @Parameter(name = "cache-maxSize", defaultValue = "1")
    String getCacheMaxSize();

    @Parameter(name = "cache-expireAfterAccessDuration")
    String getCacheExpireAfterAccessDuration();

    @Parameter(name = "cache-expireAfterAccessTimeUnit")
    String getCacheExpireAfterAccessTimeUnit();

}
