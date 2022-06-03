package org.example.component;

import org.onehippo.forge.sitemapv2.builder.DefaultDocumentSitemapEntriesBuilder;

public class HighLimitDocumentSitemapEntriesBuilder extends DefaultDocumentSitemapEntriesBuilder {

    @Override
    protected int getLimit() {
        return 10000;
    }
}
