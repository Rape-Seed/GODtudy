package com.example.godtudy.domain.todo.service;

import com.example.godtudy.domain.study.entity.Study;
import com.example.godtudy.domain.study.repository.StudyRepository;
import com.example.godtudy.domain.todo.dto.request.CreateTodoRequestDto;
import com.example.godtudy.domain.todo.dto.request.UpdateTodoRequestDto;
import com.example.godtudy.domain.todo.dto.response.TodoDto;
import com.example.godtudy.domain.todo.dto.response.TodoPagingDto;
import com.example.godtudy.domain.todo.entity.Todo;
import com.example.godtudy.domain.todo.repository.TodoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class TodoService {

    private final StudyRepository studyRepository;
    private final TodoRepository todoRepository;

    private Study findStudyByUrl(String studyUrl) {
        return studyRepository.findByUrl(studyUrl);
    }

    private Todo findTodoById(Long id) {
        return todoRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("해당하는 todo 정보가 없습니다."));
    }

    private TodoDto TodoEntityToDto(Todo todo) {
        return new TodoDto(todo);
    }

    @Transactional
    public TodoDto createTodo(String studyUrl, CreateTodoRequestDto createTodoRequestDto) {
        Study study = findStudyByUrl(studyUrl);


        Todo todo = Todo.builder()
                .title(createTodoRequestDto.getTitle())
                .content(createTodoRequestDto.getContent())
                .endDate(createTodoRequestDto.getEndDate())
                .study(study)
                .build();

        Todo savedTodo = todoRepository.save(todo);
        study.getTodoList().add(savedTodo);
        TodoDto todoDto = TodoEntityToDto(savedTodo);
        return todoDto;
    }

    @Transactional
    public TodoDto updateTodo(Long todoId, UpdateTodoRequestDto updateTodoRequestDto) {
        Todo todo = findTodoById(todoId);

        todo.updateTodo(updateTodoRequestDto);
        Todo updateTodo = todoRepository.save(todo);
        TodoDto todoDto = TodoEntityToDto(updateTodo);

        return todoDto;
    }

    @Transactional
    public Long deleteTodo(String studyUrl, Long todoId) {
        Todo todo = findTodoById(todoId);
        Study study = findStudyByUrl(studyUrl);
        study.getTodoList().remove(todo);
        todoRepository.delete(todo);

        return todo.getId();
    }

    public TodoDto getTodo(Long todoId) {
        Todo todo = findTodoById(todoId);
        return TodoEntityToDto(todo);
    }

    public TodoPagingDto getTodoList(String studyUrl, Pageable pageable) {
        Study study = findStudyByUrl(studyUrl);
        Page<Todo> todoPage = todoRepository.findTodoByStudy(study, pageable);
        Page<TodoDto> todoDtoPage = todoPage.map(entity -> new TodoDto(entity));
        return new TodoPagingDto(todoDtoPage);
    }
}
