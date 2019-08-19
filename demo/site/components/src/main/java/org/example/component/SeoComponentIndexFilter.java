package org.example.component;

import java.util.Optional;
import java.util.function.Predicate;

import org.hippoecm.hst.configuration.components.HstComponentConfiguration;
import org.hippoecm.hst.configuration.sitemap.HstSiteMap;
import org.hippoecm.hst.configuration.sitemap.HstSiteMapItem;
import org.hippoecm.hst.container.RequestContextProvider;
import org.onehippo.forge.sitemapv2.api.HstSitemapItemFilter;

public class SeoComponentIndexFilter implements HstSitemapItemFilter {

    private static final String NO_INDEX = "noindex";
    private static final String NO_INDEX_ON = "on";

    @Override
    public Predicate<HstSiteMapItem> filter() {
        return siteMapItem ->  // Get the HstComponentConfiguration of this site map item
        {
            HstComponentConfiguration hstComponentConfiguration = siteMapItem.getHstSiteMap().getSite()
                    .getComponentsConfiguration()
                    .getComponentConfiguration(siteMapItem.getComponentConfigurationId());

            if (hstComponentConfiguration != null) {
                Optional<HstComponentConfiguration> seo = hstComponentConfiguration.flattened().filter(componentConfiguration -> componentConfiguration.getParameters().containsKey(NO_INDEX))
                        .filter(componentConfiguration -> componentConfiguration.getParameter(NO_INDEX).equals(NO_INDEX_ON))
                        .findFirst();
                return !seo.isPresent();
            }
            return true;
        };
    }


}
