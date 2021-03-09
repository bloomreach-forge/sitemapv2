<#include "../include/imports.ftl">

<#if sitemap??>
 <article>
      <div class="row">
        <div class="col-md-10 col-lg-8 mx-auto">
          <div class="rte-container">
              <@sitemapTraverse node=sitemap/>
          </div>
        </div>
      </div>
 </article>
</#if>

<#macro sitemapTraverse node>
    <#if node.name!="root">
      <li>
        <a <#if node.data??>href="${node.data}"</#if> title="${node.name}">
            ${node.name}
        </a>
      </li>
    </#if>
  <ul>
      <#list node.children?keys as key>
          <#assign child=node.child(key)/>
          <@sitemapTraverse child />
      </#list>
  </ul>
</#macro>