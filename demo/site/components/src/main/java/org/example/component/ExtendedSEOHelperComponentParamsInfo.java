package org.example.component;

import org.hippoecm.hst.core.parameters.Parameter;
import org.onehippo.forge.seo.support.SEOHelperComponentParamsInfo;


public interface ExtendedSEOHelperComponentParamsInfo extends SEOHelperComponentParamsInfo {

    @Parameter(name = "noindex", required = false, description = "noindex")
    Boolean getNoindex();

}
