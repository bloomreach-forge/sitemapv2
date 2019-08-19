package org.onehippo.forge.sitemapv2.info;

import com.sun.org.apache.xpath.internal.operations.Bool;

import org.hippoecm.hst.core.parameters.DropDownList;
import org.hippoecm.hst.core.parameters.Parameter;

public interface DefaultSitemapFeedInfo {

    String ASC = "asc";
    String DESC = "desc";

    @Parameter(name = "query-limit", defaultValue = "200")
    Integer getQueryLimit();

    @Parameter(name = "useCache", defaultValue = "false")
    Boolean getUseCache();

    @Parameter(name = "query-offset", defaultValue = "0")
    Integer getQueryOffset();

    @Parameter(name = "query-ofTypes")
    String getQueryOfTypes();

    @Parameter(name = "query-primaryTypes")
    String getQueryPrimaryTypes();

    @Parameter(name = "query-notPrimaryTypes")
    String getQueryNotPrimaryTypes();

    @Parameter(name = "query-customJcrExpression")
    String getQueryCustomJcrExpression();

    @Parameter(name = "query-sortField")
    String getSortField();

    @Parameter(name = "url-changeFrequency")
    @DropDownList(value = {"ALWAYS", "HOURLY", "DAILY", "WEEKLY", "MONTHLY", "YEARLY", "NEVER"})
    String getUrlChangeFrequency();

    @Parameter(name = "url-priority")
    @DropDownList(value = {"0.1", "0.2", "0.3", "0.4", "0.5", "0.6", "0.7", "0.8", "0.9", "1.0"})
    String getUrlPriority();

    @Parameter(name = "query-sortOrder", defaultValue = DESC)
    @DropDownList(value = {ASC, DESC})
    String getSortOrder();


}
