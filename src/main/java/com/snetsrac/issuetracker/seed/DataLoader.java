package com.snetsrac.issuetracker.seed;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.github.javafaker.Faker;
import com.snetsrac.issuetracker.issue.Issue;
import com.snetsrac.issuetracker.issue.IssueRepository;
import com.snetsrac.issuetracker.issue.enumerator.IssuePriority;
import com.snetsrac.issuetracker.issue.enumerator.IssueStatus;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Profile("dev")
@Component
public class DataLoader implements CommandLineRunner {
    private static final int NUM_ISSUES = 1000;

    @Autowired
    private IssueRepository issueRepository;

    private Faker faker = new Faker();
    private Logger logger = Logger.getLogger(DataLoader.class);
    private Random random = new Random();

    @Override
    public void run(String... args) throws Exception {

        logger.info("Running DataLoader...");

        insertIssues();

        logger.info("Finished loading data.");
    }

    private void insertIssues() {
        if (issueRepository.count() == 0) {
            List<Issue> issues = new ArrayList<>();

            for (int i = 0; i < NUM_ISSUES; i++) {
                Issue issue = new Issue(
                        faker.lorem().sentence(),
                        faker.lorem().paragraph(),
                        getRandomEnumValue(IssueStatus.class),
                        getRandomEnumValue(IssuePriority.class));

                issues.add(issue);
            }

            issueRepository.saveAll(issues);
        }
    }

    private <T extends Enum<?>> T getRandomEnumValue(Class<T> clazz) {
        T[] constants = clazz.getEnumConstants();
        int i = random.nextInt(constants.length);
        return constants[i];
    }
}
