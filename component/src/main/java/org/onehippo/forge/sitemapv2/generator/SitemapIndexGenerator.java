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
import org.onehippo.forge.sitemapv2.components.model.sitemapindex.SitemapIndex;
import org.onehippo.forge.sitemapv2.components.model.sitemapindex.TSitemap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Generates the sitemap-index.xml
 */
public class SitemapIndexGenerator implements SitemapGenerator<TSitemap> {

    private static final int MAX_LIMIT = 1000;

    private static final Logger log = LoggerFactory.getLogger(SitemapIndexGenerator.class);
    private final SitemapIndex index;

    public SitemapIndexGenerator() {
        this.index = new SitemapIndex();
    }

    @SuppressWarnings("Duplicates")
    public static String toString(final SitemapIndex index) {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            JAXBContext jc = JAXBContext.newInstance(SitemapIndex.class, TSitemap.class);
            Marshaller m = jc.createMarshaller();

            TransformerHandler handler = ((SAXTransformerFactory)SAXTransformerFactory.newInstance()).newTransformerHandler();
            Transformer transformer = handler.getTransformer();
            transformer.setOutputProperty(OutputKeys.METHOD, "xml");
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", Integer.toString(2));
            handler.setResult(new StreamResult(out));

            m.marshal(index, new SiteMapCharacterEscapeHandler(handler));
            return out.toString();
        } catch (JAXBException | TransformerConfigurationException | IOException e) {
            throw new IllegalStateException("Cannot marshal the Urlset to an XML string", e);
        }
    }

    public int getSize() {
        return index.getSitemap().size();
    }

    @Override
    public String getSitemap() {
        int size = getSize();
        if (size > MAX_LIMIT) {
            log.warn("Sitemapindex size exceeds the limit of " + MAX_LIMIT);
        }
        return toString(index);
    }

    @Override
    public void add(final TSitemap sitemap) {
        index.getSitemap().add(sitemap);
    }
}