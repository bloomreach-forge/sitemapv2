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

/**
 * This Builds the sitemap.xml as a String. There are 3 main site map generators:
 * - default sitemap.xml generator {@link org.onehippo.forge.sitemapv2.generator.DefaultSitemapGenerator}
 * - sitemap index builder {@link org.onehippo.forge.sitemapv2.generator.SitemapIndexGenerator}
 * - news sitemap generator {@link org.onehippo.forge.sitemapv2.generator.SitemapNewsGenerator}
 *
 * @param <T>
 */
public interface SitemapGenerator<T> {

    void add(T url);

    int getSize();

    String getSitemap();
}
