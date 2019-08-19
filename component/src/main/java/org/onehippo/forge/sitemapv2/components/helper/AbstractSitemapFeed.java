package org.onehippo.forge.sitemapv2.components.helper;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletContext;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import org.apache.commons.lang.StringUtils;
import org.hippoecm.hst.component.support.bean.BaseHstComponent;
import org.hippoecm.hst.core.component.HstComponentException;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import org.hippoecm.hst.core.parameters.ParametersInfo;
import org.hippoecm.hst.core.request.ComponentConfiguration;
import org.onehippo.forge.sitemapv2.api.SitemapBuilder;
import org.onehippo.forge.sitemapv2.generator.SitemapGenerator;
import org.onehippo.forge.sitemapv2.info.DefaultSitemapFeedInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ParametersInfo(type = DefaultSitemapFeedInfo.class)
public abstract class AbstractSitemapFeed extends BaseHstComponent {

    private static Logger log = LoggerFactory.getLogger(AbstractSitemapFeed.class);

    //implementation of com.google.common.cache.Cache are expected to be thread-safe, and can be safely accessed
    // * by multiple concurrent threads.
    @SuppressWarnings("HippoHstThreadSafeInspection")
    private Cache<String, String> SITEMAP_CACHE;

    @Override
    public void init(final ServletContext servletContext, final ComponentConfiguration componentConfig) throws HstComponentException {
        super.init(servletContext, componentConfig);

        final Map<String, String> rawParameters = componentConfig.getRawParameters();

        if (rawParameters.containsKey("cache-enabled") && Boolean.valueOf(rawParameters.get("cache-enabled")).equals(true)) {
            final int maxSize = rawParameters.containsKey("cache-maxSize") ? Integer.valueOf(rawParameters.get("cache-maxSize")) : 1;
            final int expireAfterAccessDuration = rawParameters.containsKey("cache-expireAfterAccessDuration") ? Integer.valueOf(rawParameters.get("cache-expireAfterAccessDuration")) : 1;
            final TimeUnit expireAfterAccessTimeUnit = rawParameters.containsKey("cache-expireAfterAccessTimeUnit") ? TimeUnit.valueOf(rawParameters.get("cache-expireAfterAccessTimeUnit")) : TimeUnit.DAYS;

            SITEMAP_CACHE = CacheBuilder.newBuilder()
                    .maximumSize(maxSize)
                    .expireAfterAccess(expireAfterAccessDuration, expireAfterAccessTimeUnit)
                    .build();
        }
    }

    @Override
    public void doBeforeRender(final HstRequest request, final HstResponse response) throws HstComponentException {
        super.doBeforeRender(request, response);
        long startTime = System.nanoTime();
        String siteMap = getSiteMap(request);
        long endTime = System.nanoTime();
        long duration = (endTime - startTime);
        log.debug("ms:" + duration / 1000000);
        log.debug("mb:" + siteMap.length() / 1000000f);
        request.setAttribute("sitemap", siteMap);
    }

    /**
     * Get site map from cache or generate the site map from HST
     */
    protected String getSiteMap(final HstRequest request) {
        DefaultSitemapFeedInfo info = getComponentParametersInfo(request);
        if (Boolean.valueOf(info.getUseCache())) {
            final String mountId = request.getRequestContext().getResolvedMount().getMount().getIdentifier();
            String siteMapXml = SITEMAP_CACHE.getIfPresent(mountId);
            if (StringUtils.isNotEmpty(siteMapXml)) {
                return siteMapXml;
            }
        }
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
        log.debug("size:" + generator.getSize());
        String siteMap = generator.getSitemap();

        if (Boolean.valueOf(info.getUseCache())) {
            final String mountId = request.getRequestContext().getResolvedMount().getMount().getIdentifier();
            SITEMAP_CACHE.put(mountId, siteMap);
        }

        return siteMap;
    }

    /**
     * available for override in sub class to add or replace default builders
     *
     * @return
     */
    protected abstract Set<SitemapBuilder> getBuilders();
}
