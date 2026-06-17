package org.onehippo.forge.sitemapv2.components;

import org.junit.jupiter.api.Test;
import org.onehippo.forge.sitemapv2.components.SitemapTreeItem;

import java.util.LinkedHashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * JUnit 5 tests for {@link SitemapTreeItem}.
 */
class SitemapTreeItemTest {

    @Test
    void constructor_setsNameAndData() {
        SitemapTreeItem<String> item = new SitemapTreeItem<>("root", "data");
        assertEquals("root", item.getName());
        assertEquals("data", item.getData());
        assertTrue(item.getChildren().isEmpty());
    }

    @Test
    void add_insertsChildByName() {
        SitemapTreeItem<String> parent = new SitemapTreeItem<>("root", null);
        SitemapTreeItem<String> child  = new SitemapTreeItem<>("child", "value");
        parent.add(child);
        assertEquals(child, parent.getChildren().get("child"));
    }

    @Test
    void remove_deletesChildByName() {
        SitemapTreeItem<String> parent = new SitemapTreeItem<>("root", null);
        SitemapTreeItem<String> child  = new SitemapTreeItem<>("child", "value");
        parent.add(child);
        parent.remove(child);
        assertTrue(parent.getChildren().isEmpty());
    }

    @Test
    void child_returnsNullForMissingKey() {
        SitemapTreeItem<String> parent = new SitemapTreeItem<>("root", null);
        assertNull(parent.child("nonexistent"));
    }

    @Test
    void child_returnsCorrectChildByName() {
        SitemapTreeItem<String> parent = new SitemapTreeItem<>("root", null);
        SitemapTreeItem<String> child  = new SitemapTreeItem<>("section", "sec-data");
        parent.add(child);
        assertEquals(child, parent.child("section"));
    }

    @Test
    void setters_updateAllFields() {
        SitemapTreeItem<String> item = new SitemapTreeItem<>("old", "oldData");
        item.setName("new");
        item.setData("newData");
        LinkedHashMap<String, SitemapTreeItem<String>> children = new LinkedHashMap<>();
        item.setChildren(children);

        assertEquals("new", item.getName());
        assertEquals("newData", item.getData());
        assertEquals(children, item.getChildren());
    }

    @Test
    void constructorWithChildren_retainsProvidedChildren() {
        LinkedHashMap<String, SitemapTreeItem<String>> children = new LinkedHashMap<>();
        SitemapTreeItem<String> child = new SitemapTreeItem<>("c", "cv");
        children.put("c", child);

        SitemapTreeItem<String> parent = new SitemapTreeItem<>("root", null, children);
        assertEquals(1, parent.getChildren().size());
        assertEquals(child, parent.child("c"));
    }
}
