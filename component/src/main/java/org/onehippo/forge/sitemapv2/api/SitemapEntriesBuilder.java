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
package org.onehippo.forge.sitemapv2.api;

import org.hippoecm.hst.core.component.HstRequest;
import org.onehippo.forge.sitemapv2.builder.DefaultDocumentSitemapEntriesBuilder;
import org.onehippo.forge.sitemapv2.builder.DefaultSitemapEntriesBuilder;

public interface SitemapEntriesBuilder<T> {

    /***
     * Build the sitemap.xml according to implementation.
     * This could either be {@link DefaultDocumentSitemapEntriesBuilder} explicitly for documents
     * or {@link DefaultSitemapEntriesBuilder} explicitly for landing pages
     * @param request
     * @param componentInfo
     * @param generator
     */
    void build(final HstRequest request, final T componentInfo, final SitemapGenerator generator);

}
