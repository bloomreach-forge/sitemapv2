/*
 * Copyright 2026 Bloomreach B.V. (https://www.bloomreach.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.onehippo.forge.sitemapv2.info;

import org.hippoecm.hst.core.parameters.DropDownList;
import org.hippoecm.hst.core.parameters.Parameter;

public interface DefaultSitemapFeedInfo {

    String ASC = "asc";
    String DESC = "desc";

    @Parameter(name = "query-scopes")
    String getScopes();

    @Parameter(name = "query-exclude-scopes")
    String getExcludeScopes();

    @Parameter(name = "query-limit", defaultValue = "200")
    Integer getQueryLimit();

    @Parameter(name = "cache-enabled", defaultValue = "false")
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
