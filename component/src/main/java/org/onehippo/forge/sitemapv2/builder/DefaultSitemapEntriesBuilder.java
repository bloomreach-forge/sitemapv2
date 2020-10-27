package org.onehippo.forge.sitemapv2.builder;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

import com.google.common.collect.Sets;

import org.apache.commons.lang.StringUtils;
import org.hippoecm.hst.configuration.components.HstComponentConfiguration;
import org.hippoecm.hst.configuration.hosting.Mount;
import org.hippoecm.hst.configuration.sitemap.HstSiteMap;
import org.hippoecm.hst.configuration.sitemap.HstSiteMapItem;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.linking.HstLink;
import org.hippoecm.hst.core.linking.HstLinkCreator;
import org.hippoecm.hst.core.request.HstRequestContext;
import org.onehippo.forge.sitemapv2.api.HstSitemapItemFilter;
import org.onehippo.forge.sitemapv2.api.SitemapEntriesBuilder;
import org.onehippo.forge.sitemapv2.components.model.ChangeFrequency;
import org.onehippo.forge.sitemapv2.components.model.Url;
import org.onehippo.forge.sitemapv2.filter.DoesNotContainAnyFilter;
import org.onehippo.forge.sitemapv2.filter.DoesNotContainDefaultFilter;
import org.onehippo.forge.sitemapv2.filter.ExcludeRefIdFilter;
import org.onehippo.forge.sitemapv2.filter.IsNotAnyFilter;
import org.onehippo.forge.sitemapv2.filter.IsNotContainerResourceFilter;
import org.onehippo.forge.sitemapv2.filter.IsNotRobotsTxtFilter;
import org.onehippo.forge.sitemapv2.filter.IsNotSitemapXmlFilter;
import org.onehippo.forge.sitemapv2.filter.IsNotWildCardFilter;
import org.onehippo.forge.sitemapv2.api.SitemapGenerator;
import org.onehippo.forge.sitemapv2.info.DefaultSitemapFeedInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultSitemapEntriesBuilder implements SitemapEntriesBuilder<DefaultSitemapFeedInfo> {

    private static final HstSitemapItemFilter isNotContainerResource = new IsNotContainerResourceFilter();
    private static final HstSitemapItemFilter isNotAny = new IsNotAnyFilter();
    private static final HstSitemapItemFilter doesNotContainAny = new DoesNotContainAnyFilter();
    private static final HstSitemapItemFilter doesNotContainDefault = new DoesNotContainDefaultFilter();
    private static final HstSitemapItemFilter isNotWildCard = new IsNotWildCardFilter();
    private static final HstSitemapItemFilter isNoRobotsTxt = new IsNotRobotsTxtFilter();
    private static final HstSitemapItemFilter isNotSitemapXml = new IsNotSitemapXmlFilter();
    private static final HstSitemapItemFilter excludeRefIdFilter = new ExcludeRefIdFilter("pagenotfound");
    private static final Set<HstSitemapItemFilter> DEFAULT_HST_SITEMAP_ITEM_FILTERS = Sets.newHashSet(
            isNotContainerResource,
            isNotAny,
            doesNotContainAny,
            doesNotContainDefault,
            isNotWildCard,
            doesNotContainDefault,
            isNotWildCard,
            isNoRobotsTxt,
            isNotSitemapXml,
            excludeRefIdFilter);

    private static Logger log = LoggerFactory.getLogger(DefaultSitemapEntriesBuilder.class);

    @Override
    public void build(final HstRequest request, final DefaultSitemapFeedInfo componentInfo, final SitemapGenerator generator) {
        HstRequestContext context = request.getRequestContext();
        final HstSiteMap hstSiteMap = context.getResolvedSiteMapItem().getHstSiteMapItem().getHstSiteMap();

        Predicate<HstSiteMapItem> compositeFilter =
                getFilters().stream().map(HstSitemapItemFilter::filter)
                        .reduce(filter -> true, Predicate::and);

        hstSiteMap.getSiteMapItems().stream()
                .flatMap(this::flattenSiteMap)
                .filter(compositeFilter)
                .map(getUrlMapper(context, componentInfo))
                .forEach(generator::add);
    }

    @SuppressWarnings("Duplicates")
    protected Function<HstSiteMapItem, Url> getUrlMapper(final HstRequestContext context, final DefaultSitemapFeedInfo componentInfo) {
        HstLinkCreator hstLinkCreator = context.getHstLinkCreator();
        final Mount mount = context.getResolvedMount().getMount();
        HstSiteMap hstSiteMap = context.getResolvedSiteMapItem().getHstSiteMapItem().getHstSiteMap();
        return siteMapItem -> {
            HstLink hstLink = hstLinkCreator.create(siteMapItem, mount);
            String loc = hstLink.toUrlForm(context, true);
            if (!hstLink.isNotFound()) {
                Url url = new Url();
                url.setLoc(loc);

                HstComponentConfiguration hstComponentConfiguration = hstSiteMap.getSite()
                        .getComponentsConfiguration()
                        .getComponentConfiguration(siteMapItem.getComponentConfigurationId());

                if (hstComponentConfiguration != null) {
                    Optional<HstComponentConfiguration> latest = hstComponentConfiguration.flattened()
                            .filter(componentConfiguration -> componentConfiguration.getLastModified() != null)
                            .max(Comparator.comparing(HstComponentConfiguration::getLastModified));

                    if (latest.isPresent()) {
                        Calendar lastModified = latest.get().getLastModified();
                        url.setLastmod(lastModified);
                    }
                }

                if (StringUtils.isNotEmpty(componentInfo.getUrlChangeFrequency())) {
                    url.setChangeFrequency(ChangeFrequency.valueOf(componentInfo.getUrlChangeFrequency()));
                }
                if (StringUtils.isNotEmpty(componentInfo.getUrlPriority())) {
                    url.setPriority(new BigDecimal(componentInfo.getUrlPriority()));
                }
                return url;
            }
            return null;
        };
    }

    protected Set<HstSitemapItemFilter> getFilters() {
        return DEFAULT_HST_SITEMAP_ITEM_FILTERS;
    }

    private Stream<HstSiteMapItem> flattenSiteMap(HstSiteMapItem siteMapItem) {
        return Stream.concat(Stream.of(siteMapItem), siteMapItem.getChildren().stream().flatMap(this::flattenSiteMap));
    }

}
