package com.todo.backend.controller;

import com.todo.backend.model.Task;
import com.todo.backend.service.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.client.RestTestClient;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.springframework.http.MediaType;

@WebMvcTest(TaskController.class)
@AutoConfigureRestTestClient
public class TaskControllerTest {

    @Autowired
    private RestTestClient restTestClient;

    @MockitoBean
    private TaskService taskService;

    private Task task;
    private Task task2;

    private final Long TEST_ID = 1L;
    private final String TEST_TITLE = "test_title";
    private final String TEST_DESCRIPTION = "test_description";
    private final LocalDate TEST_DUE_DATE = LocalDate.now().plusDays(5L);

    private final LocalDate TEST_DUE_DATE_2 = LocalDate.now().plusDays(7L);

    @BeforeEach
    public void setUp() {
        task = createTestTask(TEST_ID, TEST_TITLE, TEST_DESCRIPTION, false, TEST_DUE_DATE);
        Long TEST_ID_2 = 2L;
        String TEST_TITLE_2 = "test_title";
        String TEST_DESCRIPTION_2 = "test_description";
        task2 = createTestTask(TEST_ID_2, TEST_TITLE_2, TEST_DESCRIPTION_2, false, TEST_DUE_DATE_2);
    }

    private Task createTestTask(Long id, String title, String description, boolean isComplete, LocalDate dueDate) {
        return new Task(id, title, description, isComplete, dueDate);
    }

    @Test
    public void visitingRootTasksPath_returnsAllTasks() {
        List<Task> expectedTasks = Arrays.asList(task, task2);
        when(taskService.findAll()).thenReturn(expectedTasks);

        restTestClient.get().uri("/tasks")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class);
    }

    @Test
    public void visitingTaskPath_withValidTaskID_returnsTaskWithId() {
        Long id = 1L;
        when(taskService.findTaskById(id)).thenReturn(task);

        restTestClient.get().uri("/tasks/1")
                .exchange()
                .expectStatus().isOk()
                .expectBody(Task.class)
                .consumeWith(response -> {
                    Task actualTask = response.getResponseBody();
                    assertThat(actualTask).isNotNull();
                    assertThat(actualTask.getTitle()).contains("test_title");
                    assertThat(actualTask.getDescription()).contains("description");
                    assertThat(actualTask.getId()).isEqualTo(1L);
                    assertThat(actualTask.getDueDate()).isEqualTo(TEST_DUE_DATE);
                });
    }

    @Test
    public void postNewTask_returnsCreatedTask() {
        Task newTaskRequest = createTestTask(null, "New Task", "New Description", false, LocalDate.now().plusDays(10));
        Task createdTaskResponse = createTestTask(3L, "New Task", "New Description", false, LocalDate.now().plusDays(10));

        when(taskService.createTask(any(Task.class))).thenReturn(createdTaskResponse);

        restTestClient.post().uri("/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .body(newTaskRequest)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Task.class)
                .consumeWith(response -> {
                    Task actualTask = response.getResponseBody();
                    assertThat(actualTask).isNotNull();
                    assertThat(actualTask.getId()).isEqualTo(3L);
                    assertThat(actualTask.getTitle()).isEqualTo("New Task");
                    assertThat(actualTask.getDescription()).isEqualTo("New Description");
                    assertThat(actualTask.isCompleted()).isFalse();
                    assertThat(actualTask.getDueDate()).isEqualTo(LocalDate.now().plusDays(10));
                });

        verify(taskService).createTask(any(Task.class));
    }

    @Test
    public void putMappingToCompleteRoute_withValidTaskID_returnsTaskMarkedAsComplete() {
        Task completedTaskResponse = createTestTask(TEST_ID, TEST_TITLE, TEST_DESCRIPTION, true, TEST_DUE_DATE);

        when(taskService.markTaskAsComplete(eq(TEST_ID))).thenReturn(completedTaskResponse);

        restTestClient.put().uri("/tasks/{id}/complete", TEST_ID)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Task.class)
                .consumeWith(response -> {
                    Task actualTask = response.getResponseBody();
                    assertThat(actualTask).isNotNull();
                    assertThat(actualTask.getId()).isEqualTo(TEST_ID);
                    assertThat(actualTask.getTitle()).isEqualTo(TEST_TITLE);
                    assertThat(actualTask.getDescription()).isEqualTo(TEST_DESCRIPTION);
                    assertThat(actualTask.isCompleted()).isTrue();
                    assertThat(actualTask.getDueDate()).isEqualTo(TEST_DUE_DATE);
                });

        verify(taskService).markTaskAsComplete(eq(TEST_ID));
    }

    @Test
    public void putMappingToIncompleteRoute_withValidTaskID_returnsTaskMarkedAsIncomplete() {
        task.setCompleted(true);
        Task completedTaskResponse = createTestTask(TEST_ID, TEST_TITLE, TEST_DESCRIPTION, false, TEST_DUE_DATE);

        when(taskService.markTaskAsIncomplete(eq(TEST_ID))).thenReturn(completedTaskResponse);

        restTestClient.put().uri("/tasks/{id}/incomplete", TEST_ID)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Task.class)
                .consumeWith(response -> {
                    Task actualTask = response.getResponseBody();
                    assertThat(actualTask).isNotNull();
                    assertThat(actualTask.getId()).isEqualTo(TEST_ID);
                    assertThat(actualTask.getTitle()).isEqualTo(TEST_TITLE);
                    assertThat(actualTask.getDescription()).isEqualTo(TEST_DESCRIPTION);
                    assertThat(actualTask.isCompleted()).isFalse();
                    assertThat(actualTask.getDueDate()).isEqualTo(TEST_DUE_DATE);
                });

        verify(taskService).markTaskAsIncomplete(eq(TEST_ID));
    }

    @Test
    public void deleteMappingToTaskRoute_withValidTaskID_returnsDeletedTask() {
        Task deletedTaskResponse = createTestTask(TEST_ID, TEST_TITLE, TEST_DESCRIPTION, false, TEST_DUE_DATE);

        when(taskService.deleteTaskById(eq(TEST_ID))).thenReturn(deletedTaskResponse);

        restTestClient.delete().uri("/tasks/{id}", TEST_ID)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Task.class)
                .consumeWith(response -> {
                    Task actualTask = response.getResponseBody();
                    assertThat(actualTask).isNotNull();
                    assertThat(actualTask.getId()).isEqualTo(TEST_ID);
                    assertThat(actualTask.getTitle()).isEqualTo(TEST_TITLE);
                    assertThat(actualTask.getDescription()).isEqualTo(TEST_DESCRIPTION);
                    assertThat(actualTask.isCompleted()).isFalse();
                    assertThat(actualTask.getDueDate()).isEqualTo(TEST_DUE_DATE);
                });

        verify(taskService).deleteTaskById(eq(TEST_ID));
    }
}
