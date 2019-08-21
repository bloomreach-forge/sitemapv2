package org.example;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

import org.apache.commons.lang3.RandomStringUtils;
import org.hippoecm.repository.HippoRepository;
import org.hippoecm.repository.HippoRepositoryFactory;
import org.hippoecm.repository.api.HippoWorkspace;
import org.hippoecm.repository.api.WorkflowException;
import org.hippoecm.repository.api.WorkflowManager;
import org.hippoecm.repository.util.JcrUtils;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import static org.example.BlogsDocumentCreation.createBlogDocuments;
import static org.example.ContentDocumentCreation.createContentDocuments;
import static org.example.EventDocumentCreation.createEventsDocuments;
import static org.example.NewsDocumentCreation.createNewsDocuments;

@Ignore
public class ContentCreator {

    private Session session;
    private WorkflowManager workflowManager;

    public static int getRandomNumberInRange(int min, int max) {

        if (min >= max) {
            throw new IllegalArgumentException("max must be greater than min");
        }

        Random r = new Random();
        return r.nextInt((max - min) + 1) + min;
    }

    public static <T> T random(Collection<T> coll) {
        int num = (int)(Math.random() * coll.size());
        for (T t : coll) {
            if (--num < 0) {
                return t;
            }
        }
        throw new AssertionError();
    }

    public static <T> List<T> getListFromIterator(Iterator<T> iterator) {
        // Create an empty list
        List<T> list = new ArrayList<>();

        // Add each element of iterator to the List
        iterator.forEachRemaining(list::add);

        // Return the List
        return list;
    }

    @Before
    public void setUp() throws Exception {
        HippoRepository repository = HippoRepositoryFactory.getHippoRepository("rmi://127.0.0.1:1099/hipporepository");
        this.session = repository.login("admin", "admin".toCharArray());
        this.workflowManager = ((HippoWorkspace)session.getWorkspace()).getWorkflowManager();
    }

    @Ignore
    @Test
    public void testDocumentCreation() throws RepositoryException, WorkflowException, RemoteException {
        createNewsDocuments(session, 3000);
        createContentDocuments(session, 3000);
        createEventsDocuments(session, 3000);
        createBlogDocuments(session, 3000);
        createNewsDocuments(session, 3000);
        createContentDocuments(session, 3000);
        createNewsDocuments(session, 3000);
        createEventsDocuments(session, 3000);
        createBlogDocuments(session, 3000);

        createLandingPages(300);
        createSubLandingPages(600);
    }

    @Test
    public void clear() throws RepositoryException {
        String protoTypeSitemap = "/hst:myproject/hst:configurations/myproject/hst:workspace/hst:sitemap/testabc123";
        String protoTypePage = "/hst:myproject/hst:configurations/myproject/hst:workspace/hst:pages/testabc123-contentpage";

        Node sitemap = session.getNode(protoTypeSitemap).getParent();
        NodeIterator sIt = sitemap.getNodes();
        while (sIt.hasNext()) {
            Node node = sIt.nextNode();
            if (!node.getPath().equals(protoTypeSitemap)) {
                node.remove();
            }
        }

        Node pages = session.getNode(protoTypePage).getParent();
        NodeIterator pIt = pages.getNodes();
        while (pIt.hasNext()) {
            Node node = pIt.nextNode();
            if (!node.getPath().equals(protoTypePage)) {
                node.remove();
            }
        }

        session.save();
    }

    public void createLandingPages(int amount) throws RepositoryException {
        String protoTypeSitemap = "/hst:myproject/hst:configurations/myproject/hst:workspace/hst:sitemap/testabc123";
        String protoTypePage = "/hst:myproject/hst:configurations/myproject/hst:workspace/hst:pages/testabc123-contentpage";

        String postfix = "-contentpage";

        Node pSitemap = session.getNode(protoTypeSitemap);
        Node pPage = session.getNode(protoTypePage);

        Node pages = pPage.getParent();
        Node siteMaps = pSitemap.getParent();

        for (int i = 0; i < amount; i++) {
            String name = RandomStringUtils.random(10, true, false);
            Node sitemapItem = JcrUtils.copy(session, protoTypeSitemap, siteMaps.getPath() + "/" + name + i);
            Node page = JcrUtils.copy(session, protoTypePage, pages.getPath() + "/" + name + i + postfix);

            sitemapItem.setProperty("hst:componentconfigurationid", "hst:pages/" + page.getName());
            sitemapItem.setProperty("hst:lastmodified", Calendar.getInstance());
            sitemapItem.setProperty("hst:pagetitle", name);

            page.setProperty("hst:lastmodified", Calendar.getInstance());
            session.save();
        }


    }

    public void createSubLandingPages(int amount) throws RepositoryException {
        String protoTypeSitemap = "/hst:myproject/hst:configurations/myproject/hst:workspace/hst:sitemap/testabc123";
        String protoTypePage = "/hst:myproject/hst:configurations/myproject/hst:workspace/hst:pages/testabc123-contentpage";

        String postfix = "-contentpage";

        Node pSitemap = session.getNode(protoTypeSitemap);
        Node pPage = session.getNode(protoTypePage);

        Node pages = pPage.getParent();
        Node siteMaps = pSitemap.getParent();

        for (int i = 0; i < amount; i++) {
            String name = RandomStringUtils.random(10, true, false);
            NodeIterator it = siteMaps.getNodes();

            List<Node> nodeList = getListFromIterator(it);

            Node randomSitemapitem = random(nodeList);

            while (randomSitemapitem.getPath().equals(protoTypeSitemap)) {
                randomSitemapitem = random(nodeList);
            }

            try {
                Node sitemapItem = JcrUtils.copy(session, protoTypeSitemap, randomSitemapitem.getPath() + "/" + name + i);
                Node page = JcrUtils.copy(session, protoTypePage, pages.getPath() + "/" + name + i + postfix);

                sitemapItem.setProperty("hst:componentconfigurationid", "hst:pages/" + page.getName());
                sitemapItem.setProperty("hst:lastmodified", Calendar.getInstance());
                sitemapItem.setProperty("hst:pagetitle", name);

                page.setProperty("hst:lastmodified", Calendar.getInstance());
                session.save();
            } catch (Exception e) {

            }


        }


    }

}
