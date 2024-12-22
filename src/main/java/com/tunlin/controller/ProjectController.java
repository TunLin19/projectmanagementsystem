package com.tunlin.controller;

import com.tunlin.modal.Chat;
import com.tunlin.modal.Invitation;
import com.tunlin.modal.Project;
import com.tunlin.modal.User;
import com.tunlin.request.InviteRequest;
import com.tunlin.response.MessageResponse;
import com.tunlin.service.InvitationService;
import com.tunlin.service.ProjectService;
import com.tunlin.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/project")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @Autowired
    private UserService userService;

    @Autowired
    private InvitationService invitationService;
    @GetMapping
    public ResponseEntity<List<Project>> getProject(@RequestParam(required = false) String category,
                                                    @RequestParam(required = false) String tag,
                                                    @RequestHeader("Authorization") String jwt) throws Exception {
        User user = userService.findUserProfileByJwt(jwt);
        List<Project> projects = projectService.getProjectByTeam(user,category,tag);
        return new ResponseEntity<>(projects, HttpStatus.OK);

    }

    @GetMapping("/{projectId}")
    public ResponseEntity<Project> getProjectById(@PathVariable Long projectId,
                                                  @RequestHeader("Authorization") String jwt) throws Exception {
        userService.findUserProfileByJwt(jwt);
        Project project = projectService.getProjectById(projectId);
        return new ResponseEntity<>(project, HttpStatus.OK);

    }

    @PostMapping
    public ResponseEntity<Project> createProject(@RequestBody Project project,
                                                 @RequestHeader("Authorization") String jwt) throws Exception {
        User user = userService.findUserProfileByJwt(jwt);
        Project createProject = projectService.createProject(project,user);
        return new ResponseEntity<>(createProject, HttpStatus.CREATED);

    }

    @PatchMapping("/{projectId}")
    public ResponseEntity<Project> updateProject(@RequestBody Project project,
                                                 @RequestHeader("Authorization") String jwt,
                                                 @PathVariable("projectId") Long projectId) throws Exception {
        userService.findUserProfileByJwt(jwt);
        Project updateProject = projectService.updateProject(project,projectId);
        return new ResponseEntity<>(updateProject, HttpStatus.OK);

    }

    @DeleteMapping("/{projectId}")
    public ResponseEntity<MessageResponse> deleteProject(@RequestHeader("Authorization") String jwt,
                                                         @PathVariable Long projectId) throws Exception {
        User user = userService.findUserProfileByJwt(jwt);
        projectService.deleteProject(projectId,user.getId());
        MessageResponse mes = new MessageResponse("Delete success");
        return new ResponseEntity<>(mes, HttpStatus.OK);

    }

    @GetMapping("/search")
    public ResponseEntity<List<Project>> searchProject(@RequestParam(required = false) String keyword,
                                                       @RequestHeader("Authorization") String jwt) throws Exception {
        User user = userService.findUserProfileByJwt(jwt);
        List<Project> projects = projectService.searchProject(keyword,user);
        return new ResponseEntity<>(projects, HttpStatus.OK);

    }

    @GetMapping("/{projectId}/chat")
    public ResponseEntity<Chat> getChatByProjectId(@PathVariable Long projectId,
                                                   @RequestHeader("Authorization") String jwt) throws Exception {
        userService.findUserProfileByJwt(jwt);
        Chat chat = projectService.getChatByProjectId(projectId);
        return new ResponseEntity<>(chat, HttpStatus.OK);

    }

    @PostMapping("/invite")
    public ResponseEntity<MessageResponse> inviteProject(@RequestBody InviteRequest inviteRequest,
                                                         @RequestHeader("Authorization") String jwt) throws Exception {
        userService.findUserProfileByJwt(jwt);
        invitationService.sendInvitation(inviteRequest.getEmail(),inviteRequest.getProjectId());
        MessageResponse mes = new MessageResponse("User invitation sent");
        return new ResponseEntity<>(mes, HttpStatus.OK);

    }

    @GetMapping("/accept_invitation")
    public ResponseEntity<Invitation> acceptInviteProject(@RequestParam String token,
                                                         @RequestHeader("Authorization") String jwt) throws Exception {
        User user = userService.findUserProfileByJwt(jwt);
        Invitation invitation = invitationService.acceptInvitation(token,user.getId());
        projectService.addUserToProject(invitation.getProjectId(), user.getId());
        return new ResponseEntity<>(invitation, HttpStatus.ACCEPTED);

    }

}
