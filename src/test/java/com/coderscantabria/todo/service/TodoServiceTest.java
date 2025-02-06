package com.coderscantabria.todo.service;

import com.coderscantabria.todo.model.Todo;
import com.coderscantabria.todo.repository.TodoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TodoServiceTest {

    @Mock
    private TodoRepository todoRepository;

    @InjectMocks
    private TodoService todoService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllTodos_returnsAllTodos() {
        Todo todo1 = new Todo();
        Todo todo2 = new Todo();
        when(todoRepository.findAll()).thenReturn(Arrays.asList(todo1, todo2));

        List<Todo> todos = todoService.getAllTodos();

        assertEquals(2, todos.size());
        verify(todoRepository, times(1)).findAll();
    }

    @Test
    void getTodoById_existingId_returnsTodo() {
        Todo todo = new Todo();
        when(todoRepository.findById(1L)).thenReturn(Optional.of(todo));

        Todo result = todoService.getTodoById(1L);

        assertNotNull(result);
        verify(todoRepository, times(1)).findById(1L);
    }

    @Test
    void getTodoById_nonExistingId_returnsNull() {
        when(todoRepository.findById(1L)).thenReturn(Optional.empty());

        Todo result = todoService.getTodoById(1L);

        assertNull(result);
        verify(todoRepository, times(1)).findById(1L);
    }

    @Test
    void createTodo_validTodo_returnsSavedTodo() {
        Todo todo = new Todo();
        when(todoRepository.save(todo)).thenReturn(todo);

        Todo result = todoService.createTodo(todo);

        assertNotNull(result);
        verify(todoRepository, times(1)).save(todo);
    }

    @Test
    void updateTodo_existingId_updatesAndReturnsTodo() {
        Todo existingTodo = new Todo();
        existingTodo.setTitle("Old Title");
        Todo updatedTodo = new Todo();
        updatedTodo.setTitle("New Title");
        when(todoRepository.findById(1L)).thenReturn(Optional.of(existingTodo));
        when(todoRepository.save(existingTodo)).thenReturn(existingTodo);

        Todo result = todoService.updateTodo(1L, updatedTodo);

        assertEquals("New Title", result.getTitle());
        verify(todoRepository, times(1)).findById(1L);
        verify(todoRepository, times(1)).save(existingTodo);
    }

    @Test
    void updateTodo_nonExistingId_returnsNull() {
        Todo updatedTodo = new Todo();
        when(todoRepository.findById(1L)).thenReturn(Optional.empty());

        Todo result = todoService.updateTodo(1L, updatedTodo);

        assertNull(result);
        verify(todoRepository, times(1)).findById(1L);
        verify(todoRepository, times(0)).save(any(Todo.class));
    }

    @Test
    void deleteTodo_existingId_deletesTodo() {
        when(todoRepository.existsById(1L)).thenReturn(true);

        boolean result = todoService.deleteTodo(1L);

        assertTrue(result);
        verify(todoRepository, times(1)).existsById(1L);
        verify(todoRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteTodo_nonExistingId_returnsFalse() {
        when(todoRepository.existsById(1L)).thenReturn(false);

        boolean result = todoService.deleteTodo(1L);

        assertFalse(result);
        verify(todoRepository, times(1)).existsById(1L);
        verify(todoRepository, times(0)).deleteById(1L);
    }
}
