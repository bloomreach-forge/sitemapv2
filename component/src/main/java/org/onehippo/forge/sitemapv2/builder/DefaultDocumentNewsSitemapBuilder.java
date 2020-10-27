package org.onehippo.forge.sitemapv2.builder;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.function.Function;

import javax.jcr.RepositoryException;

import com.google.common.collect.Streams;

import org.apache.commons.lang3.StringUtils;
import org.hippoecm.hst.content.beans.query.HstQuery;
import org.hippoecm.hst.content.beans.query.HstQueryResult;
import org.hippoecm.hst.content.beans.query.exceptions.QueryException;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.content.beans.standard.HippoBeanIterator;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.linking.HstLink;
import org.hippoecm.hst.core.request.HstRequestContext;
import org.onehippo.forge.sitemapv2.api.SitemapBuilder;
import org.onehippo.forge.sitemapv2.api.SitemapGenerator;
import org.onehippo.forge.sitemapv2.components.model.ChangeFrequency;
import org.onehippo.forge.sitemapv2.components.model.news.NewsUrl;
import org.onehippo.forge.sitemapv2.info.DefaultNewsSitemapFeedInfo;
import org.onehippo.forge.sitemapv2.util.QueryUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultDocumentNewsSitemapBuilder implements SitemapBuilder<DefaultNewsSitemapFeedInfo> {

    private static final int LIMIT_MAX = 1000;
    private static final String PUBLICATION_DATE_PROPERTY = "hippostdpubwf:publicationDate";
    private static final String TRANSLATION_PROPERTY_LOCALE = "hippotranslation:locale";
    private static final Logger log = LoggerFactory.getLogger(DefaultDocumentNewsSitemapBuilder.class);

    @Override
    public void build(final HstRequest request, final DefaultNewsSitemapFeedInfo componentInfo, final SitemapGenerator generator) {
        try {
            HstRequestContext context = request.getRequestContext();
            final HstQuery query = QueryUtil.constructQueryFromComponentInfo(request, getLimit(), componentInfo);

            final HstQueryResult result = query.execute();
            // Get site map items for each bean of this mount
            final HippoBeanIterator hippoBeans = result.getHippoBeans();

            Streams.stream(hippoBeans)
                    .map(getUrlMapper(context, componentInfo))
                    .forEach(generator::add);
        } catch (QueryException e) {
            log.error("A QueryException occurred with message: {}.\n See debug log for more details.", e.getMessage());
            log.debug("A QueryException occurred: ", e);
        }
    }


    @SuppressWarnings("Duplicates")
    protected Function<HippoBean, NewsUrl> getUrlMapper(final HstRequestContext context, final DefaultNewsSitemapFeedInfo componentInfo) {
        return bean -> {
            HstLink hstLink = context.getHstLinkCreator().createCanonical(bean.getNode(), context);
            if (!hstLink.isNotFound()) {
                NewsUrl url = new NewsUrl();
                String loc = hstLink.toUrlForm(context, true);
                url.setLoc(loc);
                try {
                    if (bean.getNode().hasProperty(PUBLICATION_DATE_PROPERTY)) {
                        Calendar lastMod = bean.getSingleProperty(PUBLICATION_DATE_PROPERTY);
                        url.setLastmod(lastMod);
                    }

                    if (bean.getNode().hasProperty(TRANSLATION_PROPERTY_LOCALE)) {
                        String locale = bean.getSingleProperty(TRANSLATION_PROPERTY_LOCALE);
                        url.setPublication(componentInfo.getPublicationName(), locale);

                    } else {
                        url.setPublication(componentInfo.getPublicationName(), componentInfo.getPublicationLanguageFallback());
                    }

                    if (StringUtils.isNotEmpty(componentInfo.getNewsTitlePropertyMapping()) && bean.getNode().hasProperty(componentInfo.getNewsTitlePropertyMapping())) {
                        String title = bean.getSingleProperty(componentInfo.getNewsTitlePropertyMapping());
                        url.setTitle(title);
                    } else {
                        url.setTitle(bean.getDisplayName());
                    }
                } catch (RepositoryException e) {
                    log.error("error while trying to retrieve the information from the document while creating news sitemap.xml", e);
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

    protected int getLimit() {
        return LIMIT_MAX;
    }
}
