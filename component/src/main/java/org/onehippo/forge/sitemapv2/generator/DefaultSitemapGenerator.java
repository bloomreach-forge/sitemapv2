package org.onehippo.forge.sitemapv2.generator;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stream.StreamResult;

import org.onehippo.forge.sitemapv2.api.SitemapGenerator;
import org.onehippo.forge.sitemapv2.components.model.SiteMapCharacterEscapeHandler;
import org.onehippo.forge.sitemapv2.components.model.Url;
import org.onehippo.forge.sitemapv2.components.model.Urlset;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Generated the sitemap.xml
 */
public class DefaultSitemapGenerator implements SitemapGenerator<Url> {

    private static final int MAX_LIMIT = 10000;

    private static final Logger log = LoggerFactory.getLogger(DefaultSitemapGenerator.class);
    private final Urlset urls;

    public DefaultSitemapGenerator() {
        this.urls = new Urlset();
    }

    @SuppressWarnings("Duplicates")
    public static String toString(final Urlset urls) {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            JAXBContext jc = JAXBContext.newInstance(Urlset.class, Url.class);
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
            throw new IllegalStateException("Cannot marshal the Urlset to an XML string", e);
        }
    }

    public int getSize() {
        return urls.getUrls().size();
    }

    public String getSitemap() {
        int size = getSize();
        if (size > MAX_LIMIT) {
            log.warn("Sitemap size exceeds the limit of " + MAX_LIMIT);
            log.warn("Please use an index sitemap");
        }
        return toString(urls);
    }

    @Override
    public void add(final Url url) {
        urls.add(url);
    }

    public Urlset getUrls() {
        return urls;
    }
}