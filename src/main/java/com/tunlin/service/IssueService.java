package com.tunlin.service;

import com.tunlin.modal.Issue;
import com.tunlin.modal.User;
import com.tunlin.request.IssueRequest;

import java.util.List;
import java.util.Optional;

public interface IssueService {

    Issue getIssueById(Long issueId) throws Exception;

    List<Issue> getIssueByProjectId(Long projectId) throws Exception;

    Issue createIssue(IssueRequest issue, User user) throws Exception;

    Optional<Issue> updateIssue(Long issueId, IssueRequest updateIssue, Long userId) throws Exception;

    void deleteIssue(Long issueId, Long userId) throws Exception;

    List<Issue> getIssueByAssigneeId(Long assigneeId) throws Exception;

    List<Issue> searchIssue(String title, String status, String priority) throws Exception;

    Issue addUserToIssue(Long issueId, Long userId) throws Exception;

    Issue updateStatus(Long issueId, String status) throws Exception;

}
