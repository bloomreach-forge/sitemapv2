package org.example;

import java.rmi.RemoteException;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.UUID;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

import org.apache.commons.lang3.RandomStringUtils;
import org.hippoecm.repository.HippoStdNodeType;
import org.hippoecm.repository.api.Document;
import org.hippoecm.repository.api.HippoWorkspace;
import org.hippoecm.repository.api.WorkflowException;
import org.hippoecm.repository.api.WorkflowManager;
import org.hippoecm.repository.standardworkflow.EditableWorkflow;
import org.hippoecm.repository.standardworkflow.FolderWorkflow;

import static org.example.ContentCreator.getRandomNumberInRange;

public class BlogsDocumentCreation {

    private static final String[] categories = {"life", "cms", "lorem", "ipsum"};
    private static final String[] authors = {"Luis", "Tom", "Lef", "Baris", "Minos"};

    public static void createBlogDocuments(final Session session, int amount) throws RepositoryException, WorkflowException, RemoteException {
        WorkflowManager workflowManager = ((HippoWorkspace)session.getWorkspace()).getWorkflowManager();
        String basePath = "/content/documents/myproject/blog";
        for (int i = 0; i < amount; i++) {
            createNewsDocument(workflowManager, session, basePath, getRandomNumberInRange(2000, 2019), getRandomNumberInRange(1, 12), RandomStringUtils.random(10, true, false) + i);
        }
    }

    private static void createNewsDocument(WorkflowManager workflowManager, final Session session, final String basePath, final int year, final int month, final String name) throws RepositoryException, WorkflowException, RemoteException {
        Node base = session.getNode(basePath);
        String yearString = String.valueOf(year);

        if (!base.hasNode(yearString)) {
            FolderWorkflow folderWorkflow = (FolderWorkflow)workflowManager.getWorkflow("threepane", base);
            folderWorkflow.add("new-blog-folder", "hippostd:folder", yearString);
        }

        Node yearNode = base.getNode(yearString);
        String monthString = String.valueOf(month);
        if (!yearNode.hasNode(monthString)) {
            FolderWorkflow folderWorkflow = (FolderWorkflow)workflowManager.getWorkflow("threepane", yearNode);
            folderWorkflow.add("new-blog-folder", "hippostd:folder", monthString);
        }

        Node monthNode = yearNode.getNode(monthString);
        FolderWorkflow folderWorkflow = (FolderWorkflow)workflowManager.getWorkflow("threepane", monthNode);

        String add = folderWorkflow.add("new-blog-document", "myproject:blogpost", name);
        Node documentNode = session.getNode(add);

        EditableWorkflow editableWorkflow = (EditableWorkflow)workflowManager.getWorkflow("default", documentNode);
        Document document = editableWorkflow.obtainEditableInstance();
        Node draft = document.getNode(session);
        populateRandomPropertiesOnNewsDocument(draft);
    }

    private static void populateRandomPropertiesOnNewsDocument(final Node documentNode) throws RepositoryException {
        documentNode.setProperty("myproject:title", RandomStringUtils.random(10, true, false));
        documentNode.setProperty("myproject:authornames", new String[]{authors[createRandomIntBetween(0, 4)], authors[createRandomIntBetween(0, 4)]});
        documentNode.setProperty("myproject:categories", new String[]{categories[createRandomIntBetween(0, 3)], categories[createRandomIntBetween(0, 3)]});
        documentNode.setProperty("myproject:publicationdate", randomCalendar());
        documentNode.setProperty("myproject:introduction", RandomStringUtils.random(50, true, false));

        String[] availability = new String[]{"live", "preview"};
        documentNode.setProperty("hippo:availability", availability);
        documentNode.setProperty("hippostd:stateSummary", "live");
        documentNode.setProperty(HippoStdNodeType.HIPPOSTD_STATE, HippoStdNodeType.PUBLISHED);
        documentNode.setProperty("hippostdpubwf:lastModifiedBy", "system");
        documentNode.setProperty("hippostdpubwf:createdBy", "system");
        documentNode.setProperty("hippostdpubwf:lastModificationDate", Calendar.getInstance());
        documentNode.setProperty("hippostdpubwf:creationDate", Calendar.getInstance());
        documentNode.setProperty("hippostdpubwf:publicationDate", Calendar.getInstance());
        documentNode.setProperty("hippotranslation:locale", "en");
        documentNode.setProperty("hippotranslation:id", UUID.randomUUID().toString());

        documentNode.getSession().save();
    }

    public static int createRandomIntBetween(int start, int end) {
        return start + (int)Math.round(Math.random() * (end - start));
    }

    public static LocalDate createRandomDate() {
        int startYear = 2000;
        int endYear = 2019;
        int day = createRandomIntBetween(1, 28);
        int month = createRandomIntBetween(1, 12);
        int year = createRandomIntBetween(startYear, endYear);
        return LocalDate.of(year, month, day);
    }

    public static Calendar randomCalendar() {
        LocalDate localDate = createRandomDate();
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        //assuming start of day
        calendar.set(localDate.getYear(), localDate.getMonthValue() - 1, localDate.getDayOfMonth());
        return calendar;
    }
}
