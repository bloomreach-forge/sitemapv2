# Installation

# Configuration


OOTB the following sitemap items are made available:

- abc
- def

OOTB the following components are made available:

- 123
- 456


## Guideline for configuring the Sitemap XML sitemap items  
  
  Below there is guideline written on how to configure the sitemapv2 plugin based on the amount of pages (sitemap items which have a reference to a valid component configuration id, inside and outside of the workspace) and the amount of documents (which are used in a relative content path context).
  
  Every T-Shirt sized website will have a matrix with the following:
  
- amount of pages  
- amount of documents  

And each will have the appropriate approach based on the number of pages and amount of documents.

    Please note that the channel manager starts to work slow after approximately 400 landing pages within the workspace.
  
### Small websites (single channel)  
  
| Amount of Pages | Amount of Documents  |  
|--|--|  
| 1-100 | 1-200  |  
  
Approach:  
  
- Use a single "sitemap.xml" sitemap item  
- Use the `org.onehippo.forge.sitemapv2.components.DefaultSitemapFeed` as the component class for rendering the results.  

As supplied ootb with plugin installed.
   
### Small to Medium sized websites (per channel)  
  
| Amount of Pages | Amount of Documents  |  
|--|--|  
| 50-200 | 200-1000  |  
  
Approach:  
  
- Use a single `sitemap.xm` sitemap item  
- Configure the sitemap.xml sitemap item with the `hst:componentconfigurationid` to   
`hst:components/forge-sitemapv2-default-feed-cached`
- Set the query-limit property to 1000 on the component (default is 200)

  
### Medium sized websites (per channel)  
  
| Amount of Pages | Amount of Documents  |  
|--|--|  
| 100-400 | 500-10.000  |  
  
Approach:  

Use the sitemap index sitemap item.

- Remove the `sitemap.xml` sitemap item (unusable because of the limit set in the component)
- Inspect the `sitemap-index.xml` sitemap item 
	 - configure if necessary  `sitemap-document-_default_.xml` (used for pagination of all document URLs)
	 - configure if necessary `sitemap-pages.xml` (used for pages)
  
### Medium to Large sized websites (per channel)  
  
| Amount of Pages | Amount of Documents  |  
|--|--|  
| 200-1000 | 1000-50.000  |  

Use the sitemap index sitemap item.

- Remove the `sitemap.xml` sitemap item (unusable because of the limit set in the component)
- Inspect the `sitemap-index.xml` sitemap item 
	 - configure if necessary  `sitemap-document-_default_.xml` (used for pagination of all document URLs)
		 - Update the limit and apply caching if necessary
	 - configure if necessary `sitemap-pages.xml` (used for pages)
  
### Large websites (per channel):  
  
| Amount of Pages | Amount of Documents  |  
|--|--|  
| 500-1500 | 50.000-1.000.000  |

Use the sitemap index sitemap item.

- Remove the `sitemap.xml` sitemap item (unusable because of the limit set in the component)
- Inspect the `sitemap-index.xml` sitemap item 
	 - configure if necessary  `sitemap-document-_default_.xml` (used for pagination of all document URLs)
		 - Update the limit and apply caching if necessary
	 - configure if necessary `sitemap-pages.xml` (used for pages)

## Component Configuration

The component which are set on the `hst:componentconfigurationid` of the sitemap.xml sitemap items have several available component properties available. Documentation also available: `org.onehippo.forge.sitemapv2.info.DefaultSitemapFeedInfo`

Any feed implementing `org.onehippo.forge.sitemapv2.components.helper.AbstractSitemapFeed` will have the following properties available:

| property | use | default |
|--|--|--|
| query-limit  | set a limit on the document feed query, maximum value is 1000  | 200 |
| query-offset | set an offset on the document feed query. | 0
| query-ofTypes | filter query where subtypes are included. |
| query-primaryTypes | filter query where specific primary types are included, but subtypes are ***not*** included |
| query-notPrimaryTypes | filter query where specific primary types are not included |
| query-customJcrExpression | filter query by adding specific jcr expression. **Note:** be careful that the query is not intensive on the repository
| query-sortOrder | Order on how to sort the query | Descending
| query-sortField | Field on how to sort the query |
| url-changeFrequency | URL priority in the sitemap feed i.e. 0.1, 0.2 ... 0.9 and 1.0 |
| url-changeFrequency | URL frequency in the sitemap feed i.e. ALWAYS, HOURLY, DAILY, WEEKLY, MONTHLY, YEARLY, NEVER |
| cache-enabled | Enable caching | false
| cache-maxSize | Specifies the maximum number of entries the cache may contain. Increase if there are more endpoints and more channels | 1
| cache-expireAfterAccessDuration | Specifies that each entry should be automatically removed from the cache once a fixed duration has elapsed after the entry's creation, the most recent replacement of its value, or its last access. | 1
| cache-expireAfterAccessTimeUnit | i.e. SECONDS, MINUTES, DAYS, MONTHS.. (See java.util.concurrent.TimeUnit) | DAYS



# Extending

See demo project for examples on extending. In the demo project there is a case implemented where it will check a specific component and property set on a landing page to exclude it from the sitemap xml. In the demo project there is also an example of excluding document types from the feed but also properties on a document (see author document type, filtered if a boolean is checked).

All extensions in the demo are available: `org.example.component.*`