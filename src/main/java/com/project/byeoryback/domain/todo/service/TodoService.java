package com.project.byeoryback.domain.todo.service;

import com.project.byeoryback.domain.todo.dto.TodoRequestDto;
import com.project.byeoryback.domain.todo.dto.TodoResponseDto;
import com.project.byeoryback.domain.todo.entity.Todo;
import com.project.byeoryback.domain.todo.repository.TodoRepository;
import com.project.byeoryback.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.time.LocalDate;
import java.time.LocalTime;

@Service
@RequiredArgsConstructor
public class TodoService {

    private final TodoRepository todoRepository;

    @Transactional(readOnly = true)
    public List<TodoResponseDto> getTodos(User user) {
        return todoRepository.findAllByUserOrderByStartDateDesc(user).stream()
                .map(TodoResponseDto::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public TodoResponseDto createTodo(User user, TodoRequestDto request) {
        Todo todo = Todo.builder()
                .user(user)
                .title(request.getTitle())
                .completed(request.getCompleted() != null && request.getCompleted())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .allDay(request.getAllDay() != null && request.getAllDay())
                .build();

        return TodoResponseDto.from(todoRepository.save(todo));
    }

    @Transactional
    public TodoResponseDto updateTodo(User user, Long todoId, TodoRequestDto request) {
        Todo todo = todoRepository.findById(todoId)
                .orElseThrow(() -> new IllegalArgumentException("Todo not found with id: " + todoId));

        if (!todo.getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("Unauthorized access to todo");
        }

        String title = request.getTitle() != null ? request.getTitle() : todo.getTitle();
        boolean completed = request.getCompleted() != null ? request.getCompleted() : todo.isCompleted();
        LocalDate startDate = request.getStartDate() != null ? request.getStartDate() : todo.getStartDate();
        LocalDate endDate = request.getEndDate() != null ? request.getEndDate() : todo.getEndDate();

        // Handle time fields: if null in request, keep existing?
        // Based on "Include only changed fields", yes.
        // If one wants to unset time, they might need to send explicit null, but DTO
        // handles null as missing.
        // Usually for Todo apps, clearing time is less common via partial update
        // without explicit intent.
        // We will assume "if null, keep existing" for simplicity as per standard
        // PATCH-like behavior for PUT with partial data.
        LocalTime startTime = request.getStartTime() != null ? request.getStartTime() : todo.getStartTime();
        LocalTime endTime = request.getEndTime() != null ? request.getEndTime() : todo.getEndTime();

        boolean allDay = request.getAllDay() != null ? request.getAllDay() : todo.isAllDay();

        todo.update(title, completed, startDate, endDate, startTime, endTime, allDay);

        return TodoResponseDto.from(todo);
    }

    @Transactional
    public void deleteTodo(User user, Long todoId) {
        Todo todo = todoRepository.findById(todoId)
                .orElseThrow(() -> new IllegalArgumentException("Todo not found with id: " + todoId));

        if (!todo.getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("Unauthorized access to todo");
        }

        todoRepository.delete(todo);
    }
}
