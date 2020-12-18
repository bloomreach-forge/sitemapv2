package org.onehippo.forge.sitemapv2.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hippoecm.hst.content.beans.query.HstQuery;
import org.hippoecm.hst.content.beans.query.builder.HstQueryBuilder;
import org.hippoecm.hst.content.beans.query.exceptions.FilterException;
import org.hippoecm.hst.content.beans.query.filter.Filter;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.content.beans.standard.HippoDocument;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.request.HstRequestContext;
import org.hippoecm.hst.util.PathUtils;
import org.onehippo.forge.sitemapv2.info.DefaultSitemapFeedInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;

public class QueryUtil {

    private static Logger log = LoggerFactory.getLogger(QueryUtil.class);

    private QueryUtil() {
    }


    @SuppressWarnings("HippoHstFilterInspection")
    public static HstQuery constructQueryFromArgs(final HstRequest request,
                                                  final String scopes,
                                                  final String excludeScopes,
                                                  final int limit,
                                                  final int queryLimit,
                                                  final int offset,
                                                  final String queryPrimaryTypes,
                                                  final String queryOffTypes,
                                                  final String querySortField,
                                                  final String querySortOrder,
                                                  final String queryNotPrimaryTypes,
                                                  final String queryCustomJcrExpression

    ) {
        HstRequestContext context = request.getRequestContext();
        HippoBean[] beanScopes = getBeanScopes(context, scopes);
        HippoBean[] beanExcludeScopes = getBeanScopes(context, excludeScopes);
        HstQueryBuilder hstQueryBuilder = HstQueryBuilder
                .create(beanScopes.length > 0 ? beanScopes : new HippoBean[]{context.getSiteContentBaseBean()})
                .excludeScopes(beanExcludeScopes.length > 0 ? beanExcludeScopes : null)
                .limit(queryLimit > limit ? limit : queryLimit)
                .offset(offset);

        if (StringUtils.isNotEmpty(queryPrimaryTypes)) {
            hstQueryBuilder.ofPrimaryTypes(MatcherUtils.getCommaSeparatedValues(queryPrimaryTypes));
        }

        if (StringUtils.isNotEmpty(queryOffTypes)) {
            hstQueryBuilder.ofTypes(MatcherUtils.getCommaSeparatedValues(queryOffTypes));
        } else if (StringUtils.isEmpty(queryOffTypes) && StringUtils.isEmpty(queryPrimaryTypes)) {
            //default set hippo document
            hstQueryBuilder.ofTypes(HippoDocument.class);
        }

        if (StringUtils.isEmpty(querySortField)) {
            hstQueryBuilder.orderBy(HstQueryBuilder.Order.fromString(querySortOrder), querySortField);
        }

        final HstQuery query = hstQueryBuilder.build();

        if (StringUtils.isNotEmpty(queryNotPrimaryTypes)) {
            String[] queryNotPrimaryTypesArray = MatcherUtils.getCommaSeparatedValues(queryNotPrimaryTypes);
            Filter queryFilter = getFilter(query);
            Arrays.stream(queryNotPrimaryTypesArray).forEach(primaryType -> {
                try {
                    queryFilter.addNotEqualTo("jcr:primaryType", primaryType);
                } catch (FilterException e) {
                    log.error("error while adding multiple primary type constraints", e);
                }
            });
        }

        if (StringUtils.isNotEmpty(queryCustomJcrExpression)) {
            Filter queryFilter = getFilter(query);
            queryFilter.addJCRExpression(queryCustomJcrExpression);
        }
        return query;
    }

    private static HippoBean[] getBeanScopes(final HstRequestContext context, final String scopes){
        if(StringUtils.isNotBlank(scopes)) {
            final String[] stringScopes = MatcherUtils.getCommaSeparatedValues(scopes);
            final HippoBean siteBean = context.getSiteContentBaseBean();
            List<HippoBean> scopeBeansList = new ArrayList<>();

            for(String scope: stringScopes){
                if (!Strings.isNullOrEmpty(scope)) {
                    final String scopePath = PathUtils.normalizePath(scope);
                    log.debug("Looking for bean {}", scopePath);
                    HippoBean beanScope = siteBean.getBean(scopePath);
                    if (beanScope != null) {
                        scopeBeansList.add(beanScope);
                    }
                }
            }
            return scopeBeansList.toArray(new HippoBean[0]);
        }
        return new HippoBean[]{};
    }

    private static final Filter getFilter(final HstQuery query) {
        Filter filter = (Filter)query.getFilter();
        if (filter == null) {
            filter = query.createFilter();
            query.setFilter(filter);
        }
        return filter;
    }

    public static HstQuery constructQueryFromComponentInfo(final HstRequest request, final int limit, final DefaultSitemapFeedInfo componentInfo) {
        return constructQueryFromArgs(request,
                componentInfo.getScopes(),
                componentInfo.getExcludeScopes(),
                limit,
                componentInfo.getQueryLimit(),
                componentInfo.getQueryOffset(),
                componentInfo.getQueryPrimaryTypes(),
                componentInfo.getQueryOfTypes(),
                componentInfo.getSortField(),
                componentInfo.getSortOrder(),
                componentInfo.getQueryNotPrimaryTypes(),
                componentInfo.getQueryCustomJcrExpression());
    }
}
