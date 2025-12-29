package com.project.byeoryback.domain.todo.controller;

import com.project.byeoryback.domain.todo.dto.TodoRequestDto;
import com.project.byeoryback.domain.todo.dto.TodoResponseDto;
import com.project.byeoryback.domain.todo.service.TodoService;
import com.project.byeoryback.global.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/todos")
@RequiredArgsConstructor
public class TodoController {

    private final TodoService todoService;

    @GetMapping
    public ResponseEntity<List<TodoResponseDto>> getTodos(@AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(todoService.getTodos(userDetails.getUser()));
    }

    @PostMapping
    public ResponseEntity<TodoResponseDto> createTodo(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody TodoRequestDto request) {
        return ResponseEntity.ok(todoService.createTodo(userDetails.getUser(), request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TodoResponseDto> updateTodo(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long id,
            @RequestBody TodoRequestDto request) {
        return ResponseEntity.ok(todoService.updateTodo(userDetails.getUser(), id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTodo(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long id) {
        todoService.deleteTodo(userDetails.getUser(), id);
        return ResponseEntity.ok().build();
    }
}
