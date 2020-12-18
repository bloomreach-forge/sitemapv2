package org.onehippo.forge.sitemapv2.components;

import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.hippoecm.hst.component.support.bean.BaseHstComponent;
import org.hippoecm.hst.configuration.components.HstComponentConfiguration;
import org.hippoecm.hst.configuration.components.HstComponentsConfiguration;
import org.hippoecm.hst.configuration.sitemap.HstSiteMap;
import org.hippoecm.hst.configuration.sitemap.HstSiteMapItem;
import org.hippoecm.hst.content.beans.query.HstQuery;
import org.hippoecm.hst.content.beans.query.HstQueryResult;
import org.hippoecm.hst.content.beans.query.exceptions.QueryException;
import org.hippoecm.hst.core.component.HstComponentException;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import org.hippoecm.hst.core.linking.HstLink;
import org.hippoecm.hst.core.linking.HstLinkCreator;
import org.hippoecm.hst.core.request.HstRequestContext;
import org.onehippo.forge.sitemapv2.api.SitemapGenerator;
import org.onehippo.forge.sitemapv2.components.model.sitemapindex.TSitemap;
import org.onehippo.forge.sitemapv2.generator.SitemapIndexGenerator;
import org.onehippo.forge.sitemapv2.util.QueryUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultSitemapIndexFeed extends BaseHstComponent {

    private static Logger log = LoggerFactory.getLogger(DefaultSitemapIndexFeed.class);

    @Override
    public void doBeforeRender(final HstRequest request, final HstResponse response) throws HstComponentException {
        super.doBeforeRender(request, response);

        final HstSiteMap hstSiteMap = request.getRequestContext().getResolvedSiteMapItem().getHstSiteMapItem().getHstSiteMap();
        final SitemapGenerator generator = new SitemapIndexGenerator();
        final HstComponentsConfiguration componentsConfiguration = hstSiteMap.getSite()
                .getComponentsConfiguration();

        hstSiteMap.getSiteMapItems()
                .stream()
                .flatMap(this::flattenSiteMap)
                .filter(siteMapItem -> siteMapItem.getRefId() != null && siteMapItem.getRefId().startsWith("sitemap-")).collect(Collectors.toSet())
                .forEach(addToIndexSitemap(request, componentsConfiguration, generator));

        request.setAttribute("sitemap", generator.getSitemap());
    }

    protected Consumer<HstSiteMapItem> addToIndexSitemap(final HstRequest request, final HstComponentsConfiguration componentsConfiguration, final SitemapGenerator generator) {
        return siteMapItem -> {
            HstRequestContext context = request.getRequestContext();
            HstLinkCreator linkCreator = context.getHstLinkCreator();
            if ("sitemap-document-offset".equals(siteMapItem.getRefId())) {
                HstComponentConfiguration componentConfiguration = componentsConfiguration.getComponentConfiguration(siteMapItem.getComponentConfigurationId());

                Map<String, String> parameters = componentConfiguration.getParameters();

                HstQuery hstQuery = QueryUtil.constructQueryFromArgs(request,
                        parameters.get("query-scopes"),
                        parameters.get("query-exclude-scopes"),
                        1,
                        1,
                        0,
                        parameters.get("query-primaryTypes"),
                        parameters.get("query-ofTypes"),
                        parameters.get("query-sortField"),
                        parameters.get("query-sortOrder"),
                        parameters.get("query-notPrimaryTypes"),
                        parameters.get("query-customJcrExpression"));

                try {
                    HstQueryResult result = hstQuery.execute();
                    int totalSize = result.getTotalSize();
                    int pageSize = Integer.valueOf(getParameter(parameters, "query-limit", "200"));

                    int pages = totalSize / pageSize;
                    if ((totalSize % pageSize) > 0) {
                        pages++;
                    }

                    for (int page = 1; page <= pages; page++) {
                        String id = siteMapItem.getId();
                        String path = id.replace("_default_", String.valueOf(getStartOffset(page, pageSize, totalSize)));

                        TSitemap sitemap = new TSitemap();
                        HstLink hstLink = linkCreator.create(path, context.getResolvedMount().getMount());
                        sitemap.setLoc(hstLink.toUrlForm(context, true));
                        generator.add(sitemap);
                    }
                } catch (QueryException e) {
                    log.error("error while getting the sitemap link of document", e);
                }
            } else if ("sitemap-pages".equals(siteMapItem.getRefId())) {
                HstLink hstLink = linkCreator.create(siteMapItem, context.getResolvedMount().getMount());
                TSitemap sitemap = new TSitemap();
                sitemap.setLoc(hstLink.toUrlForm(context, true));
                generator.add(sitemap);
            } else {
                log.warn(siteMapItem.getId() + " should not be in the result set?");
            }
        };
    }

    /**
     * Get where result offset should start NOTE: it's zero based
     *
     * @return int
     */
    public int getStartOffset(int pageNumber, int pageSize, int total) {
        int start = (pageNumber - 1) * pageSize;
        if (start >= total) {
            start = 0;
        }
        return start;
    }

    public String getParameter(final Map<String, String> parameters, final String property, final String def) {
        return parameters.get(property) != null ? parameters.get(property) : def;
    }

    private Stream<HstSiteMapItem> flattenSiteMap(HstSiteMapItem siteMapItem) {
        return Stream.concat(Stream.of(siteMapItem), siteMapItem.getChildren().stream().flatMap(this::flattenSiteMap));
    }
}
