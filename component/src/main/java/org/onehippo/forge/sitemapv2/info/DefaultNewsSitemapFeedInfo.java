package org.onehippo.forge.sitemapv2.info;

import org.hippoecm.hst.core.parameters.Parameter;

public interface DefaultNewsSitemapFeedInfo extends DefaultSitemapFeedInfo {

    @Parameter(name = "news-publicationName", defaultValue = "Bloomreach (change in configuration)", required = true)
    String getPublicationName();

    @Parameter(name = "news-defaultPublicationLanguage", defaultValue = "en")
    String getPublicationLanguageFallback();

    @Parameter(name = "news-titlePropertyMapping", required = true)
    String getNewsTitlePropertyMapping();

}
