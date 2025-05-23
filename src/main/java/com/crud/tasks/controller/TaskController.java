package com.crud.tasks.controller;

import com.crud.tasks.domain.TaskDto;
import java.util.List;

import org.springframework.web.bind.annotation.*;
import com.crud.tasks.service.DbService;
import com.crud.tasks.mapper.TaskMapper;
import org.springframework.beans.factory.annotation.Autowired;
import com.crud.tasks.domain.Task;
import org.springframework.http.MediaType;
import com.crud.tasks.exception.TaskNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;

@CrossOrigin("*")
@RestController
@RequestMapping("/v1/tasks")
public class TaskController {
    private final DbService service;
    private final TaskMapper taskMapper;

    @Autowired
    public TaskController(DbService service, TaskMapper taskMapper) {
        this.service = service;
        this.taskMapper = taskMapper;
    }

    @GetMapping
    public ResponseEntity<List<TaskDto>> getAllTasks() {
        List<Task> tasks = service.getAllTasks();
        return ResponseEntity.ok(taskMapper.mapToTaskDtoList(tasks));
    }

    @GetMapping(value = "{taskId}")
    public ResponseEntity<TaskDto> getTask(@PathVariable Long taskId) throws TaskNotFoundException {
        return ResponseEntity.ok(taskMapper.mapToTaskDto(service.getTask(taskId)));
    }

    @DeleteMapping("/{taskId}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long taskId) throws TaskNotFoundException {
        service.deleteById(taskId);
        return ResponseEntity.ok().build();
    }
    @PutMapping
    public ResponseEntity<TaskDto> updateTask(@RequestBody TaskDto taskDto) {
        Task task = taskMapper.mapToTask(taskDto);
        Task savedTask = service.saveTask(task);
        return ResponseEntity.ok(taskMapper.mapToTaskDto(savedTask));
    }
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void>  createTask(@RequestBody TaskDto taskDto) {
        Task task = taskMapper.mapToTask(taskDto);
        service.saveTask(task);
        return ResponseEntity.ok().build();
    }

}