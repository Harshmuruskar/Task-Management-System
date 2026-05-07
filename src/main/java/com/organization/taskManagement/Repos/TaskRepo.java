package com.organization.taskManagement.Repos;

import com.organization.taskManagement.Model.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepo extends JpaRepository<Task, Long> {

}
