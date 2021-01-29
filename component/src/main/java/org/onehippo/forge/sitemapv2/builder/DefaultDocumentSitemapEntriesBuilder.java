package org.onehippo.forge.sitemapv2.builder;

import com.google.common.collect.Streams;
import org.apache.commons.lang.StringUtils;
import org.hippoecm.hst.content.beans.query.HstQuery;
import org.hippoecm.hst.content.beans.query.HstQueryResult;
import org.hippoecm.hst.content.beans.query.exceptions.QueryException;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.content.beans.standard.HippoBeanIterator;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.linking.HstLink;
import org.hippoecm.hst.core.request.HstRequestContext;
import org.onehippo.forge.sitemapv2.api.SitemapEntriesBuilder;
import org.onehippo.forge.sitemapv2.api.SitemapGenerator;
import org.onehippo.forge.sitemapv2.components.model.ChangeFrequency;
import org.onehippo.forge.sitemapv2.components.model.Url;
import org.onehippo.forge.sitemapv2.info.DefaultSitemapFeedInfo;
import org.onehippo.forge.sitemapv2.util.QueryUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.RepositoryException;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Objects;
import java.util.function.Function;

public class DefaultDocumentSitemapEntriesBuilder implements SitemapEntriesBuilder<DefaultSitemapFeedInfo> {

    private static final int LIMIT_MAX = 1000;
    private static final String PUBLICTION_DATE_PROPERTY = "hippostdpubwf:publicationDate";
    private static final Logger log = LoggerFactory.getLogger(DefaultDocumentSitemapEntriesBuilder.class);

    @Override
    public void build(final HstRequest request, final DefaultSitemapFeedInfo componentInfo, final SitemapGenerator generator) {
        try {
            HstRequestContext context = request.getRequestContext();
            final HstQuery query = QueryUtil.constructQueryFromComponentInfo(request, getLimit(), componentInfo);

            final HstQueryResult result = query.execute();
            final int totalSize = result.getTotalSize();
            if (totalSize > getLimit()) {
                log.warn("total size of query is bigger then the limit, please update the max limit" +
                        " or create an sitemap index with additional sitemap.xml resources");
            }
            // Get site map items for each bean of this mount
            final HippoBeanIterator hippoBeans = result.getHippoBeans();

            Streams.stream(hippoBeans)
                    .map(getUrlMapper(context, componentInfo))
                    .filter(Objects::nonNull)
                    .forEach(generator::add);
        } catch (QueryException e) {
            log.error("A QueryException occurred with message: {}.\n See debug log for more details.", e.getMessage());
            log.debug("A QueryException occurred: ", e);
        }
    }


    @SuppressWarnings("Duplicates")
    protected Function<HippoBean, Url> getUrlMapper(final HstRequestContext context, final DefaultSitemapFeedInfo componentInfo) {
        return bean -> {
            HstLink hstLink = context.getHstLinkCreator().createCanonical(bean.getNode(), context);
            if (!hstLink.isNotFound()) {
                Url url = new Url();
                String loc = hstLink.toUrlForm(context, true);
                url.setLoc(loc);
                try {
                    if (bean.getNode().hasProperty(PUBLICTION_DATE_PROPERTY)) {
                        Calendar lastMod = bean.getSingleProperty(PUBLICTION_DATE_PROPERTY);
                        url.setLastmod(lastMod);
                    }
                } catch (RepositoryException e) {
                    log.error("error while trying to retrieve the last publication date of document while creating sitemap.xml", e);
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
