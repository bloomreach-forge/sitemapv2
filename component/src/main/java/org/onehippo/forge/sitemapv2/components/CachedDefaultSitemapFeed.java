package org.onehippo.forge.sitemapv2.components;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletContext;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import org.apache.commons.lang.StringUtils;
import org.hippoecm.hst.core.component.HstComponentException;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.parameters.ParametersInfo;
import org.hippoecm.hst.core.request.ComponentConfiguration;
import org.onehippo.forge.sitemapv2.info.CachedDefaultSitemapFeedInfo;

@ParametersInfo(type = CachedDefaultSitemapFeedInfo.class)
public class CachedDefaultSitemapFeed extends DefaultSitemapFeed {

    //implementation of com.google.common.cache.Cache are expected to be thread-safe, and can be safely accessed
    // * by multiple concurrent threads.
    @SuppressWarnings("HippoHstThreadSafeInspection")
    private Cache<String, String> SITEMAP_CACHE;

    @Override
    public void init(final ServletContext servletContext, final ComponentConfiguration componentConfig) throws HstComponentException {
        super.init(servletContext, componentConfig);

        final Map<String, String> rawParameters = componentConfig.getRawParameters();

        final int maxSize = rawParameters.containsKey("cache-maxSize") ? Integer.valueOf(rawParameters.get("cache-maxSize")) : 1;
        final int expireAfterAccessDuration = rawParameters.containsKey("cache-expireAfterAccessDuration") ? Integer.valueOf(rawParameters.get("cache-expireAfterAccessDuration")) : 1;
        final TimeUnit expireAfterAccessTimeUnit = rawParameters.containsKey("cache-expireAfterAccessTimeUnit") ? TimeUnit.valueOf(rawParameters.get("cache-expireAfterAccessTimeUnit")) : TimeUnit.DAYS;

        SITEMAP_CACHE = CacheBuilder.newBuilder()
                .maximumSize(maxSize)
                .expireAfterAccess(expireAfterAccessDuration, expireAfterAccessTimeUnit)
                .build();
    }

    @Override
    protected String getSiteMap(final HstRequest request) {
        final String mountId = request.getRequestContext().getResolvedMount().getMount().getIdentifier();

        String siteMapXml = SITEMAP_CACHE.getIfPresent(mountId);
        if (StringUtils.isNotEmpty(siteMapXml)) {
            return siteMapXml;
        }

        return super.getSiteMap(request);
    }

    @Override
    protected String buildSiteMap(final HstRequest request) {
        final String mountId = request.getRequestContext().getResolvedMount().getMount().getIdentifier();
        String siteMap = super.buildSiteMap(request);
        SITEMAP_CACHE.put(mountId, siteMap);
        return siteMap;
    }

}
