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
package org.onehippo.forge.sitemapv2.components;

import java.util.Set;

import com.google.common.collect.Sets;

import org.hippoecm.hst.core.component.HstComponentException;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import org.hippoecm.hst.core.parameters.ParametersInfo;
import org.onehippo.forge.sitemapv2.api.SitemapEntriesBuilder;
import org.onehippo.forge.sitemapv2.builder.DefaultSitemapEntriesBuilder;
import org.onehippo.forge.sitemapv2.info.DefaultSitemapFeedInfo;

@ParametersInfo(type = DefaultSitemapFeedInfo.class)
public class HstSitemapItemsFeed extends AbstractSitemapFeed {

    //Builder for hst sitemap items inside and outside of workspace
    private static final SitemapEntriesBuilder<DefaultSitemapFeedInfo> HST_SITEMAP_SITEMAP_BUILDER = new DefaultSitemapEntriesBuilder();
    private static final Set<SitemapEntriesBuilder> HST_SITEMAP_BUILDER = Sets.newHashSet(HST_SITEMAP_SITEMAP_BUILDER);

    @SuppressWarnings("Duplicates")
    @Override
    public void doBeforeRender(final HstRequest request, final HstResponse response) throws HstComponentException {
        super.doBeforeRender(request, response);
    }

    /**
     * available for override in sub class to add or replace default builders
     *
     * @return
     */
    protected Set<SitemapEntriesBuilder> getBuilders() {
        return HST_SITEMAP_BUILDER;
    }
}
