package com.organization.taskManagement.Repository;

import com.organization.taskManagement.Model.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {

}
