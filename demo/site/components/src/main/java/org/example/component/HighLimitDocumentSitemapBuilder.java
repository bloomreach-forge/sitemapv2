package org.example.component;

import org.onehippo.forge.sitemapv2.builder.DefaultDocumentSitemapBuilder;

public class HighLimitDocumentSitemapBuilder extends DefaultDocumentSitemapBuilder {

    @Override
    protected int getLimit() {
        return 10000;
    }
}
