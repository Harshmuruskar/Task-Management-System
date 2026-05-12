package com.organization.taskManagement.Repository;

import com.organization.taskManagement.Model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
