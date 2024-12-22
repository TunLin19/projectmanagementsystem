package com.tunlin.controller;

import com.tunlin.dto.IssueDto;
import com.tunlin.modal.Issue;
import com.tunlin.modal.User;
import com.tunlin.request.IssueRequest;
import com.tunlin.response.AuthResponse;
import com.tunlin.response.MessageResponse;
import com.tunlin.service.IssueService;
import com.tunlin.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/issues")
public class IssueController {

    @Autowired
    private IssueService issueService;

    @Autowired
    private UserService userService;

    @GetMapping("/{issueId}")
    public ResponseEntity<Issue> getIssueById(@PathVariable Long issueId) throws Exception {

        Issue issue = issueService.getIssueById(issueId);
        return ResponseEntity.ok(issue);

    }

    @GetMapping("/project/{projectId}")
    public ResponseEntity<List<Issue>> getIssueByProjectId(@PathVariable Long projectId) throws Exception {

        List<Issue> issues = issueService.getIssueByProjectId(projectId);
        return ResponseEntity.ok(issues);

    }

    @PostMapping
    public ResponseEntity<IssueDto> createIssue(@RequestBody IssueRequest issue,
                                                @RequestHeader("Authorization") String jwt) throws Exception {

        User tokenUser = userService.findUserProfileByJwt(jwt);
        User user = userService.findUserById(tokenUser.getId());

        Issue createIssue = issueService.createIssue(issue,tokenUser);
        IssueDto issueDto = new IssueDto();
        issueDto.setAssignee(createIssue.getAssignee());
        issueDto.setDescription(createIssue.getDescription());
        issueDto.setDueDate(createIssue.getDueDate());
        issueDto.setId(createIssue.getId());
        issueDto.setPriority(createIssue.getPriority());
        issueDto.setProject(createIssue.getProject());
        issueDto.setStatus(createIssue.getStatus());
        issueDto.setTitle(createIssue.getTitle());
        issueDto.setTags(createIssue.getTags());
        return ResponseEntity.ok(issueDto);

    }

    @DeleteMapping("/{issueId}")
    public ResponseEntity<MessageResponse> deleteIssue(@PathVariable Long issueId,
                                                       @RequestHeader("Authorization") String jwt) throws Exception {

        User user = userService.findUserProfileByJwt(jwt);
        issueService.deleteIssue(issueId, user.getId());

        MessageResponse mes = new MessageResponse();
        mes.setMessage("Issue deleted");
        return ResponseEntity.ok(mes);

    }

    @PutMapping("/{issueId}/assignee/{userId}")
    public ResponseEntity<Issue> addUserToIssue(@PathVariable Long issueId,
                                                @PathVariable Long userId) throws Exception {

        Issue issue = issueService.addUserToIssue(issueId, userId);
        return ResponseEntity.ok(issue);

    }

    @GetMapping("/assignee/{assigneeId}")
    public ResponseEntity<List<Issue>> getIssueByAssigneeId(@PathVariable Long assigneeId) throws Exception {

        List<Issue> issues = issueService.getIssueByAssigneeId(assigneeId);
        return ResponseEntity.ok(issues);

    }

    @PutMapping("/{issueId}/status/{status}")
    public ResponseEntity<Issue> updateIssueStatus(@PathVariable String status,
                                                   @PathVariable Long issueId) throws Exception {

        Issue issue = issueService.updateStatus(issueId,status);
        return ResponseEntity.ok(issue);

    }

}
