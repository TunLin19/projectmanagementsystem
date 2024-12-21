package com.tunlin.service.impl;

import com.tunlin.modal.Issue;
import com.tunlin.modal.Project;
import com.tunlin.modal.User;
import com.tunlin.repository.IssueRepository;
import com.tunlin.request.IssueRequest;
import com.tunlin.service.IssueService;
import com.tunlin.service.ProjectService;
import com.tunlin.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class IssueServiceImpl implements IssueService {

    @Autowired
    private IssueRepository  issueRepository;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private UserService userService;

    @Override
    public Issue getIssueById(Long issueId) throws Exception {

        Optional<Issue> issue = issueRepository.findById(issueId);
        if (issue.isPresent()){
            return issue.get();
        }
        throw  new Exception("No issue with is issueId" + issueId);
    }

    @Override
    public List<Issue> getIssueByProjectId(Long projectId) throws Exception {

        return issueRepository.findByProjectID(projectId);

    }

    @Override
    public Issue createIssue(IssueRequest issue, User user) throws Exception {

        Project project = projectService.getProjectById(issue.getProjectId());

        Issue createIssue = new Issue();
        createIssue.setTitle(issue.getTitle());
        createIssue.setDescription(issue.getDescription());
        createIssue.setStatus(issue.getStatus());
        createIssue.setProjectID(issue.getProjectId());
        createIssue.setPriority(issue.getPriority());
        createIssue.setDueDate(issue.getDueDate());
        createIssue.setProject(project);
        return issueRepository.save(createIssue);

    }

    @Override
    public Optional<Issue> updateIssue(Long issueId, IssueRequest updateIssue, Long userId) throws Exception {
        return Optional.empty();
    }

    @Override
    public void deleteIssue(Long issueId, Long userId) throws Exception {

        getIssueById(issueId);
        issueRepository.deleteById(issueId);

    }

    @Override
    public List<Issue> getIssueByAssigneeId(Long assigneeId) throws Exception {
        return null;
    }

    @Override
    public List<Issue> searchIssue(String title, String status, String priority) throws Exception {
        return null;
    }

    @Override
    public Issue addUserToIssue(Long issueId, Long userId) throws Exception {

        User user = userService.findUserById(userId);
        Issue issue = getIssueById(issueId);
        issue.setAssignee(user);
        return issueRepository.save(issue);

    }

    @Override
    public Issue updateStatus(Long issueId, String status) throws Exception {

        Issue issue = getIssueById(issueId);
        issue.setStatus(status);
        return issueRepository.save(issue);

    }
}
