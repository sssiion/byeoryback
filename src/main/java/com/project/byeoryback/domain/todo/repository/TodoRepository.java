package com.project.byeoryback.domain.todo.repository;

import com.project.byeoryback.domain.todo.entity.Todo;
import com.project.byeoryback.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TodoRepository extends JpaRepository<Todo, Long> {
    List<Todo> findAllByUser(User user);

    List<Todo> findAllByUserOrderByStartDateDesc(User user);

    void deleteByUser(User user);
}
