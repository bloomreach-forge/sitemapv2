package org.onehippo.forge.sitemapv2.components;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletContext;

import org.hippoecm.hst.core.component.HstComponentException;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import org.hippoecm.hst.core.parameters.ParametersInfo;
import org.hippoecm.hst.core.request.ComponentConfiguration;
import org.onehippo.forge.sitemapv2.components.model.Url;
import org.onehippo.forge.sitemapv2.generator.DefaultSitemapGenerator;
import org.onehippo.forge.sitemapv2.info.DefaultSitemapFeedInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

@ParametersInfo(type = DefaultSitemapFeedInfo.class)
public class StructuredDocumentFeed extends DocumentFeed {

    public static final String PREVIEW_URL_PATTERN = "^/(site)/(_cmsinternal)/(.*)";
    public static final String LIVE_URL_PATTERN = "^(https://|http://)[^/]+/(site/)?(.*)";

    private static final Logger LOG = LoggerFactory.getLogger(StructuredDocumentFeed.class);
    private Cache<String, SitemapTreeItem<String>> STRUCTURAL_SITEMAP_CACHE;

    @Override
    public void init(final ServletContext servletContext, final ComponentConfiguration componentConfig) throws HstComponentException {
        super.init(servletContext, componentConfig);

        final Map<String, String> rawParameters = componentConfig.getRawParameters();
        if (rawParameters.containsKey("cache-enabled") && Boolean.valueOf(rawParameters.get("cache-enabled")).equals(true)) {
            final int maxSize = rawParameters.containsKey("cache-maxSize") ? Integer.parseInt(rawParameters.get("cache-maxSize")) : 1;
            final int expireAfterAccessDuration = rawParameters.containsKey("cache-expireAfterAccessDuration") ? Integer.parseInt(rawParameters.get("cache-expireAfterAccessDuration")) : 1;
            final TimeUnit expireAfterAccessTimeUnit = rawParameters.containsKey("cache-expireAfterAccessTimeUnit") ? TimeUnit.valueOf(rawParameters.get("cache-expireAfterAccessTimeUnit")) : TimeUnit.DAYS;

            STRUCTURAL_SITEMAP_CACHE = CacheBuilder.newBuilder()
                    .maximumSize(maxSize)
                    .expireAfterAccess(expireAfterAccessDuration, expireAfterAccessTimeUnit)
                    .build();
        }
    }

    @Override
    public void doBeforeRender(final HstRequest request, final HstResponse response) throws HstComponentException {
        long startTime = System.nanoTime();
        SitemapTreeItem<String> siteMap = retrieveSitemap(request);
        long endTime = System.nanoTime();
        if (LOG.isDebugEnabled()) {
            long duration = (endTime - startTime);
            LOG.debug("time to generate sitemap in ms:" + duration / 1000000);
        }
        request.setAttribute("sitemap", siteMap);
    }

    protected SitemapTreeItem<String> retrieveSitemap(final HstRequest request){
        DefaultSitemapFeedInfo info = getComponentParametersInfo(request);
        if (info.getUseCache()) {
            final String cacheKey = getCacheKey(request);
            SitemapTreeItem<String> cachedSitemap = STRUCTURAL_SITEMAP_CACHE.getIfPresent(cacheKey);
            return cachedSitemap!=null ? cachedSitemap : buildStructuredSiteMap(request);
        }
        return buildStructuredSiteMap(request);
    }

    protected SitemapTreeItem<String> buildStructuredSiteMap(final HstRequest request) {
        final DefaultSitemapGenerator generator = (DefaultSitemapGenerator) getSitemapGenerator();
        DefaultSitemapFeedInfo info = getComponentParametersInfo(request);
        getBuilders().forEach(sitemapBuilder -> sitemapBuilder.build(request, info, generator));
        if (LOG.isDebugEnabled()) {
            LOG.debug("sitemap size:" + generator.getSize());
        }
        Set<Url> urlSet = generator.getUrls().getUrls();
        SitemapTreeItem<String> rootNode = new SitemapTreeItem<>("root", null);

        Pattern pattern;
        if (request.getRequestContext().isChannelManagerPreviewRequest()) {
            pattern = Pattern.compile(PREVIEW_URL_PATTERN);
        } else {
            pattern = Pattern.compile(LIVE_URL_PATTERN);
        }

        urlSet.forEach( url -> transform(pattern, url, rootNode));

        if (info.getUseCache()) {
            final String cacheKey = getCacheKey(request);
            STRUCTURAL_SITEMAP_CACHE.put(cacheKey, rootNode);
        }

        return rootNode;
    }

    protected void transform(final Pattern pattern, final Url url, final SitemapTreeItem<String> rootNode){
        Matcher matcher = pattern.matcher(url.getLoc());
        if (matcher.matches()) {
            String[] pathElements = matcher.group(3).split("/");
            SitemapTreeItem<String> currentNode = rootNode;
            SitemapTreeItem<String> nextNode;
            int total = pathElements.length;
            for (int i = 0; i < total; i++) {
                nextNode = currentNode.getChildren().get(pathElements[i]);
                if (nextNode == null) {
                    if (i == total - 1 ) {
                        currentNode.getChildren().put(pathElements[i], new SitemapTreeItem<>(pathElements[i], url.getLoc()));
                    } else {
                        SitemapTreeItem<String> node = new SitemapTreeItem<>(pathElements[i], null);
                        currentNode.getChildren().put(pathElements[i], node);
                        currentNode = node;
                    }
                } else {
                    if (i == total - 1 ) {
                        nextNode.setData(url.getLoc());
                    } else {
                        currentNode = nextNode;
                    }
                }
            }
        }
    }

}
