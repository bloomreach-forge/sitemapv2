package org.onehippo.forge.sitemapv2.generator;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stream.StreamResult;

import org.onehippo.forge.sitemapv2.api.SitemapGenerator;
import org.onehippo.forge.sitemapv2.components.model.SiteMapCharacterEscapeHandler;
import org.onehippo.forge.sitemapv2.components.model.news.NewsUrl;
import org.onehippo.forge.sitemapv2.components.model.news.NewsUrlset;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Generated the sitemap.xml
 */
public class SitemapNewsGenerator implements SitemapGenerator<NewsUrl> {

    private static final int MAX_LIMIT = 1000;

    private static final Logger log = LoggerFactory.getLogger(SitemapNewsGenerator.class);
    private final NewsUrlset urls;

    public SitemapNewsGenerator() {
        this.urls = new NewsUrlset();
    }

    @SuppressWarnings("Duplicates")
    public static String toString(final NewsUrlset urls) {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            JAXBContext jc = JAXBContext.newInstance(NewsUrlset.class, NewsUrl.class);
            Marshaller m = jc.createMarshaller();

            TransformerHandler handler = ((SAXTransformerFactory)SAXTransformerFactory.newInstance()).newTransformerHandler();
            Transformer transformer = handler.getTransformer();
            transformer.setOutputProperty(OutputKeys.METHOD, "xml");
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", Integer.toString(2));
            handler.setResult(new StreamResult(out));

            m.marshal(urls, new SiteMapCharacterEscapeHandler(handler));
            return out.toString();
        } catch (JAXBException | TransformerConfigurationException | IOException e) {
            throw new IllegalStateException("Cannot marshal the NewsUrlset to an XML string", e);
        }
    }

    public int getSize() {
        return urls.getUrls().size();
    }

    public String getSitemap() {
        int size = getSize();
        if (size > MAX_LIMIT) {
            log.warn("News Sitemap size exceeds the limit of " + MAX_LIMIT);
            log.warn("Please configure component parameters");
        }
        return toString(urls);
    }


    @Override
    public void add(final NewsUrl url) {
        urls.addUrl(url);
    }
}