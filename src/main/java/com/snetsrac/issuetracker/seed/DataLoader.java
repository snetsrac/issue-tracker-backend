package com.snetsrac.issuetracker.seed;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import com.github.javafaker.Faker;
import com.snetsrac.issuetracker.issue.Issue;
import com.snetsrac.issuetracker.issue.IssueRepository;
import com.snetsrac.issuetracker.issue.Issue.Priority;
import com.snetsrac.issuetracker.issue.Issue.Status;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Profile("dev")
@Component
public class DataLoader implements CommandLineRunner {
    private static final int NUM_ISSUES = 1000;

    @Value("${DEMO_USER_ID}")
    private String DEMO_USER_ID;

    @Value("${DEMO_SUBMITTER_ID}")
    private String DEMO_SUBMITTER_ID;

    @Value("${DEMO_DEVELOPER_ID}")
    private String DEMO_DEVELOPER_ID;

    @Value("${DEMO_MANAGER_ID}")
    private String DEMO_MANAGER_ID;

    @Value("${DEMO_ADMIN_ID}")
    private String DEMO_ADMIN_ID;

    @Autowired
    private IssueRepository issueRepository;

    private static Faker faker = new Faker();
    private static Logger logger = Logger.getLogger(DataLoader.class);
    private static Random random = new Random();
    private String[] submitterIds;
    private String[] assigneeIds;

    @Override
    public void run(String... args) throws Exception {

        logger.info("Running DataLoader...");
        
        submitterIds = new String[] { DEMO_SUBMITTER_ID, DEMO_DEVELOPER_ID, DEMO_MANAGER_ID, DEMO_ADMIN_ID };
        assigneeIds = new String[] { DEMO_DEVELOPER_ID, DEMO_MANAGER_ID, DEMO_ADMIN_ID };
        insertIssues();

        logger.info("Finished loading data.");
    }

    private void insertIssues() {
        if (issueRepository.count() == 0) {
            List<Issue> issues = new ArrayList<>();

            for (int i = 0; i < NUM_ISSUES; i++) {
                Issue issue = new Issue();

                issue.setTitle(faker.lorem().sentence());
                issue.setDescription(String.join("\n", faker.lorem().paragraphs(random.nextInt(4) + 1)));
                issue.setStatus(getRandomEnumValue(Status.class));
                issue.setPriority(getRandomEnumValue(Priority.class));
                issue.setSubmitterId(getRandomSubmitterId());
                issue.setAssigneeIds(getRandomAssigneeIds());
                issue.setCreatedAt(Instant.now());

                issues.add(issue);
            }

            issueRepository.saveAll(issues);
        }
    }

    private static <T extends Enum<?>> T getRandomEnumValue(Class<T> clazz) {
        T[] constants = clazz.getEnumConstants();
        int i = random.nextInt(constants.length);
        return constants[i];
    }

    private String getRandomSubmitterId() {
        int i = random.nextInt(submitterIds.length);
        return submitterIds[i];
    }

    private Set<String> getRandomAssigneeIds() {
        int n = random.nextInt(assigneeIds.length + 1);
        Set<String> ids = new HashSet<>();

        for (int i = 0; i < n; i++) {
            ids.add(assigneeIds[i]);
        }

        return ids;
    }
}
