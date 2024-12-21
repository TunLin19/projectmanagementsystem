package com.tunlin.repository;

import com.tunlin.modal.Comments;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentsRepository extends JpaRepository<Comments, Long> {

    List<Comments> findCommentsByIssueId(Long issueId);

    List<Comments> findByIssueId(Long issueId);

}
