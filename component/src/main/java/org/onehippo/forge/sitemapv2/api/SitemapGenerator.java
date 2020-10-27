package org.onehippo.forge.sitemapv2.api;

public interface SitemapGenerator<T> {

    void add(T url);

    int getSize();

    String getSitemap();
}
