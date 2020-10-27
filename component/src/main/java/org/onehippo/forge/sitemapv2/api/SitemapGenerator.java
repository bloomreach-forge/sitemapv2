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
