package com.tunlin.controller;

import com.tunlin.modal.Comments;
import com.tunlin.modal.User;
import com.tunlin.request.CommentRequest;
import com.tunlin.response.MessageResponse;
import com.tunlin.service.CommentService;
import com.tunlin.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
public class CommentsController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private UserService userService;

    @PostMapping()
    public ResponseEntity<Comments> createComment(@RequestBody CommentRequest commentRequest,
                                                  @RequestHeader("Authorization") String jwt) throws Exception {

        User user = userService.findUserProfileByJwt(jwt);
        Comments createComment = commentService.createComment(commentRequest.getIssueId(),user.getId(), commentRequest.getContent());
        return new ResponseEntity<>(createComment, HttpStatus.CREATED);

    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<MessageResponse> deleteComment(@PathVariable Long commentId,
                                                         @RequestHeader("Authorization") String jwt) throws Exception {

        User user = userService.findUserProfileByJwt(jwt);
        commentService.deleteComment(commentId,user.getId());
        MessageResponse mes = new MessageResponse("Comment delete successfully");
        return ResponseEntity.ok(mes);

    }

    @GetMapping("/{issueId}")
    public ResponseEntity<List<Comments>> getCommentByIssueId(@PathVariable Long issueId){

        List<Comments> comments = commentService.findByCommentByIssueId(issueId);
        return ResponseEntity.ok(comments);

    }

}
