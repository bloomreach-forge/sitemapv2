package org.onehippo.forge.sitemapv2.components;

import org.junit.Assert;
import org.junit.Test;
import org.onehippo.forge.sitemapv2.components.model.Url;

import java.util.regex.Pattern;

public class TestStructuredDocumentFeed {

    @Test
    public void testTransform() {
        StructuredDocumentFeed component = new StructuredDocumentFeed();
        Pattern liveUrlPattern = Pattern.compile(StructuredDocumentFeed.LIVE_URL_PATTERN);
        Pattern previewUrlPattern = Pattern.compile(StructuredDocumentFeed.PREVIEW_URL_PATTERN);
        Url url = new Url();

        SitemapTreeItem<String> rootNode = new SitemapTreeItem<>("root", null);
        url.setLoc("http://www.example.com/about/us");
        component.transform(liveUrlPattern, url, rootNode);
        Assert.assertEquals(url.getLoc(), rootNode.getChildren().get("about").getChildren().get("us").getData());

        rootNode = new SitemapTreeItem<>("root", null);
        url.setLoc("https://www.example.com/info.html");
        component.transform(liveUrlPattern, url, rootNode);
        Assert.assertEquals(url.getLoc(), rootNode.getChildren().get("info.html").getData());

        rootNode = new SitemapTreeItem<>("root", null);
        url.setLoc("https://www.example.com/about/us");
        component.transform(liveUrlPattern, url, rootNode);
        Assert.assertEquals(url.getLoc(), rootNode.getChildren().get("about").getChildren().get("us").getData());

        rootNode = new SitemapTreeItem<>("root", null);
        url.setLoc("https://example.com/about/us/");
        component.transform(liveUrlPattern, url, rootNode);
        Assert.assertEquals(url.getLoc(), rootNode.getChildren().get("about").getChildren().get("us").getData());

        rootNode = new SitemapTreeItem<>("root", null);
        url.setLoc("https://www.example.com/site/about/us");
        component.transform(liveUrlPattern, url, rootNode);
        Assert.assertEquals(url.getLoc(), rootNode.getChildren().get("about").getChildren().get("us").getData());
        Assert.assertNull(rootNode.getChildren().get("site"));

        rootNode = new SitemapTreeItem<>("root", null);
        url.setLoc("http://localhost:8080/site/about/us");
        component.transform(liveUrlPattern, url, rootNode);
        Assert.assertEquals(url.getLoc(), rootNode.getChildren().get("about").getChildren().get("us").getData());
        Assert.assertNull(rootNode.getChildren().get("site"));

        rootNode = new SitemapTreeItem<>("root", null);
        url.setLoc("https://www.example.com/");
        component.transform(liveUrlPattern, url, rootNode);
        Assert.assertEquals(url.getLoc(), rootNode.getChildren().get("").getData());

        rootNode = new SitemapTreeItem<>("root", null);
        url.setLoc("https://www.example.com");
        component.transform(liveUrlPattern, url, rootNode);
        Assert.assertTrue(rootNode.getChildren().isEmpty());

        rootNode = new SitemapTreeItem<>("root", null);
        url.setLoc("/site/_cmsinternal/about/us");
        component.transform(previewUrlPattern, url, rootNode);
        Assert.assertEquals(url.getLoc(), rootNode.getChildren().get("about").getChildren().get("us").getData());
        Assert.assertNull(rootNode.getChildren().get("site"));
        Assert.assertNull(rootNode.getChildren().get("_cmsinternal"));

        rootNode = new SitemapTreeItem<>("root", null);
        url.setLoc("/site/_cmsinternal/");
        component.transform(previewUrlPattern, url, rootNode);
        Assert.assertEquals(url.getLoc(), rootNode.getChildren().get("").getData());
    }
}
