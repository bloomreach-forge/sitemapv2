@XmlSchema(
        namespace = "http://www.sitemaps.org/schemas/sitemap/0.9",
        elementFormDefault = XmlNsForm.QUALIFIED,
        xmlns = {
                @XmlNs(prefix = "", namespaceURI = "http://www.sitemaps.org/schemas/sitemap/0.9"),
                @XmlNs(prefix = "news", namespaceURI = "http://www.google.com/schemas/sitemap-news/0.9")
        }
)
package org.onehippo.forge.sitemapv2.components.model.news;

import jakarta.xml.bind.annotation.XmlNs;
import jakarta.xml.bind.annotation.XmlNsForm;
import jakarta.xml.bind.annotation.XmlSchema;
