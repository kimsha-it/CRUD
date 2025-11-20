package com.example.todoapp.service;

import com.example.todoapp.dto.TodoDto;
import com.example.todoapp.repository.TodoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TodoService {
    private final TodoRepository todoRepository;

    public TodoService(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    public List<TodoDto> getAllTodos() {
        return todoRepository.findAll();
    }

    public TodoDto getTodoById(Long id) {
        return todoRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("todo not found!!"));
    }

    public void deleteTodoById(Long id) {
        getTodoById(id);
        todoRepository.deleteById(id);
    }

    public TodoDto upDateTodoById(Long id, TodoDto newtodo) {
        TodoDto originTodo = getTodoById(id);

        validateTitle(newtodo.getTitle());

        originTodo.setTitle(newtodo.getTitle());
        originTodo.setContent(newtodo.getContent());
        originTodo.setCompleted(newtodo.isCompleted());

        return todoRepository.save(originTodo);
    }

    public TodoDto createTodo(TodoDto todo) {
        validateTitle(todo.getTitle());
        return todoRepository.save(todo);
    }

    public List<TodoDto> searchTodos(String keyword) {
        return todoRepository.findByTitleContaining(keyword);
    }

    public List<TodoDto> getTodosByCompleted(boolean completed) {
        return todoRepository.findByCompleted(completed);
    }

    public TodoDto toggleCompleted(Long id) {
        TodoDto todo  = getTodoById(id);
        todo.setCompleted(!todo.isCompleted());
        return todoRepository.save(todo);
    }

    private void validateTitle(String title) {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("제목은 비어 있을 수 없습니다.");
        }

        if (title.length() > 50) {
            throw new IllegalArgumentException("제목은 50자를 초과할 수 없습니다.");
        }
    }
}
