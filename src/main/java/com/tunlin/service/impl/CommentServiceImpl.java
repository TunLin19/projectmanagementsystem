package com.tunlin.service.impl;

import com.tunlin.modal.Comments;
import com.tunlin.modal.Issue;
import com.tunlin.modal.User;
import com.tunlin.repository.CommentsRepository;
import com.tunlin.repository.IssueRepository;
import com.tunlin.repository.UserRepository;
import com.tunlin.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentsRepository commentsRepository;

    @Autowired
    private IssueRepository issueRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public Comments createComment(Long issueId, Long userId, String content) throws Exception {

        Optional<Issue> issueOptional = issueRepository.findById(issueId);
        Optional<User> userOptional = userRepository.findById(userId);

        if(issueOptional.isEmpty()){
            throw new Exception("Issue not found with id"+issueId);
        }
        if(userOptional.isEmpty()){
            throw new Exception("User not found with id"+userId);
        }
        Issue issue = issueOptional.get();
        User user = userOptional.get();

        Comments comments = new Comments();
        comments.setIssue(issue);
        comments.setUser(user);
        comments.setCreateDateTime(LocalDateTime.now());
        comments.setContent(content);

        Comments savedComment = commentsRepository.save(comments);
        issue.getComments().add(savedComment);
        return savedComment;

    }

    @Override
    public void deleteComment(Long commentId, Long userId) throws Exception {

        Optional<Comments> commentsOptional = commentsRepository.findById(commentId);
        Optional<User> userOptional = userRepository.findById(userId);

        if(commentsOptional.isEmpty()){
            throw new Exception("Comment not found with id"+commentId);
        }
        if(userOptional.isEmpty()){
            throw new Exception("User not found with id"+userId);
        }
        Comments comments = commentsOptional.get();
        User user = userOptional.get();

        if (comments.getUser().equals(user)){
            commentsRepository.delete(comments);
        }else {
            throw new Exception("User doesn't have permission to delete this comment");
        }

    }

    @Override
    public List<Comments> findByCommentByIssueId(Long issueId) {
        return commentsRepository.findByIssueId(issueId);
    }
}
