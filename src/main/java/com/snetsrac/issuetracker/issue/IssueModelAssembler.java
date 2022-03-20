package com.snetsrac.issuetracker.issue;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

@Component
public class IssueModelAssembler implements RepresentationModelAssembler<Issue, EntityModel<Issue>> {
    @Override
    public EntityModel<Issue> toModel(Issue issue) { 
        return EntityModel.of(issue,
                linkTo(methodOn(IssueController.class).getIssueById(issue.getId())).withSelfRel(),
                linkTo(IssueController.class).withRel("issues"));
    }
}
