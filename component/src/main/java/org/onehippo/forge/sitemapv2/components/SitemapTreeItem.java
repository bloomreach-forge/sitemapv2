/*
 * Copyright 2026 Bloomreach B.V. (https://www.bloomreach.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.onehippo.forge.sitemapv2.components;

import java.util.LinkedHashMap;

public class SitemapTreeItem<T> {
    private String name;
    private T data;
    private LinkedHashMap<String, SitemapTreeItem<T>> children = new LinkedHashMap<>();

    public SitemapTreeItem(final String name, final T data) {
        this.name = name;
        this.data = data;
    }

    public SitemapTreeItem(final String name, final T data, final LinkedHashMap<String, SitemapTreeItem<T>> children) {
        this.name = name;
        this.data = data;
        this.children = children;
    }

    public void add(SitemapTreeItem<T> node){
        this.children.put(node.name, node);
    }

    public void remove(SitemapTreeItem<T> node){
        this.children.remove(node.name);
    }

    public SitemapTreeItem<T> child(final String name){
        return this.children.get(name);
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public T getData() {
        return data;
    }

    public void setData(final T data) {
        this.data = data;
    }

    public LinkedHashMap<String, SitemapTreeItem<T>> getChildren() {
        return children;
    }

    public void setChildren(final LinkedHashMap<String, SitemapTreeItem<T>> children) {
        this.children = children;
    }

}
