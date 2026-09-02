package com.todo.backend.service;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

import org.mockito.junit.jupiter.MockitoExtension;

import com.todo.backend.model.Task;
import com.todo.backend.repository.TaskRepository;

@ExtendWith(MockitoExtension.class)
public class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskService taskService;

    private Task task;
    private Task task2;

    private final Long TEST_ID = 1L;
    private final String TEST_TITLE = "test_title";
    private final String TEST_DESCRIPTION = "test_description";
    private final LocalDate TEST_DUE_DATE = LocalDate.now().plusDays(5L);

    private final Long TEST_ID_2 = 2L;
    private final String TEST_TITLE_2 = "test_title";
    private final String TEST_DESCRIPTION_2 = "test_description";
    private final LocalDate TEST_DUE_DATE_2 = LocalDate.now().plusDays(7L);

    @BeforeEach
    public void setUp() {
        task = createTestTask(TEST_ID, TEST_TITLE, TEST_DESCRIPTION, false, TEST_DUE_DATE);
        task2 = createTestTask(TEST_ID_2, TEST_TITLE_2, TEST_DESCRIPTION_2, false, TEST_DUE_DATE_2);
    }

    private Task createTestTask(Long id, String title, String description, boolean isComplete, LocalDate dueDate) {
        return new Task(id, title, description, isComplete, dueDate);
    }

    @Test
    public void testFindAll_returnsAllTasks() {
        List<Task> expectedTasks = Arrays.asList(task, task2);
        when(taskRepository.findAll()).thenReturn(expectedTasks);

        List<Task> actualTasks = taskService.findAll();
        
        assertNotNull(actualTasks);
        assertEquals(expectedTasks, actualTasks);
    }

    @Test
    public void testFindTaskById_whenTaskExists_returnsTask() {
        Long existingId = 1L;
        when(taskRepository.findById(TEST_ID)).thenReturn(Optional.of(task));
        
        Task foundTask = taskService.findTaskById(existingId);
        
        assertNotNull(foundTask);
        assertEquals(foundTask, task);
        assertEquals(foundTask.getId(), existingId);
        assertEquals(foundTask.getTitle(), TEST_TITLE);
        assertEquals(foundTask.getDescription() , TEST_DESCRIPTION);
        assertEquals(foundTask.getDueDate(), TEST_DUE_DATE);
        verify(taskRepository, times(1)).findById(existingId);
    }

    @Test
    public void testFindTaskById_whenTaskDoesNotExist_returnsNull() {
        Long nonExistentId = 99L;
        when(taskRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        Task foundTask = taskService.findTaskById(nonExistentId);

        assertNull(foundTask);
        verify(taskRepository, times(1)).findById(nonExistentId);
    }

    @Test
    public void testChangeCompletedStatus_setToTrue() {
        task.setCompleted(false);

        when(taskRepository.save(any(Task.class))).thenReturn(task);

        Task updatedTask = taskService.changeCompletedStatus(task, true);
        assertTrue(updatedTask.isCompleted());
        verify(taskRepository, times(1)).save(task);
    }

    @Test
    public void testChangeCompletedStatus_setToFalse() {
        task.setCompleted(true);

        when(taskRepository.save(any(Task.class))).thenReturn(task);

        Task updatedTask = taskService.changeCompletedStatus(task, false);
        assertFalse(updatedTask.isCompleted());
        verify(taskRepository, times(1)).save(task);
    }

    @Test
    public void testMarkTaskAsComplete_whenGivenExistingTaskID_successfullyUpdates() {
        Long existingId = 1L;
        when(taskRepository.findById(existingId)).thenReturn(Optional.of(task));
        when(taskRepository.save(any(Task.class))).thenAnswer(returnsFirstArg());

        Task updatedTask = taskService.markTaskAsComplete(existingId);
        assertNotNull(updatedTask);
        assertTrue(updatedTask.isCompleted());

        verify(taskRepository, times(1)).save(task);
    }

    @Test
    public void testMarkTaskAsComplete_whenGivenNonExistingTaskID_throwsError() throws IllegalArgumentException {
        Long nonExistingId = 99L;
        when(taskRepository.findById(nonExistingId)).thenReturn(Optional.empty());

        IllegalArgumentException thrown = assertThrows(
                IllegalArgumentException.class,
                () -> taskService.markTaskAsComplete(nonExistingId)
        );

        assertTrue(thrown.getMessage().contains("Task with id " + nonExistingId + " not found"));
        verify(taskRepository, times(1)).findById(nonExistingId);
        verify(taskRepository, times(0)).save(any(Task.class));
    }

    @Test
    public void testMarkTaskAsComplete_whenGivenTaskObject_successfullyUpdates() {
        when(taskRepository.save(any(Task.class))).thenAnswer(returnsFirstArg());

        Task updatedTask = taskService.markTaskAsComplete(task);
        assertNotNull(updatedTask);
        assertTrue(updatedTask.isCompleted());

        verify(taskRepository, times(1)).save(task);
    }

    @Test
    public void testMarkTaskAsInomplete_whenGivenExistingTaskID_successfullyUpdates() {
        task.setCompleted(true);
        Long existingId = 1L;
        when(taskRepository.findById(existingId)).thenReturn(Optional.of(task));
        when(taskRepository.save(any(Task.class))).thenAnswer(returnsFirstArg());

        Task updatedTask = taskService.markTaskAsIncomplete(existingId);
        assertNotNull(updatedTask);
        assertFalse(updatedTask.isCompleted());

        verify(taskRepository, times(1)).save(task);
    }

    @Test
    public void testMarkTaskAsIncomplete_whenGivenNonExistingTaskID_throwsError() throws IllegalArgumentException {
        task.setCompleted(true);
        Long nonExistingId = 99L;
        when(taskRepository.findById(nonExistingId)).thenReturn(Optional.empty());

        IllegalArgumentException thrown = assertThrows(
                IllegalArgumentException.class,
                () -> taskService.markTaskAsIncomplete(nonExistingId)
        );

        assertTrue(thrown.getMessage().contains("Task with id " + nonExistingId + " not found"));
        verify(taskRepository, times(1)).findById(nonExistingId);
        verify(taskRepository, times(0)).save(any(Task.class));
    }

    @Test
    public void testMarkTaskAsIncomplete_whenGivenTaskObject_successfullyUpdates() {
        task.setCompleted(true);
        when(taskRepository.save(any(Task.class))).thenAnswer(returnsFirstArg());

        Task updatedTask = taskService.markTaskAsIncomplete(task);
        assertNotNull(updatedTask);
        assertFalse(updatedTask.isCompleted());

        verify(taskRepository, times(1)).save(task);
    }

    @Test
    public void testChangeDescription_whenGivenExistingID_successfullyUpdates() {
        Long existingId = 1L;
        String newDescription = "new_description";
        when(taskRepository.findById(existingId)).thenReturn(Optional.of(task));
        when(taskRepository.save(any(Task.class))).thenAnswer(returnsFirstArg());

        Task updatedTask = taskService.changeDescription(existingId, newDescription);

        assertNotNull(updatedTask);
        assertEquals(newDescription, updatedTask.getDescription());

        verify(taskRepository, times(1)).save(task);
    }

    @Test
    public void testChangeDescription_whenGivenNonExistingTaskID_throwsError() throws IllegalArgumentException {
        String newDescription = "new_description";
        Long nonExistingId = 99L;
        when(taskRepository.findById(nonExistingId)).thenReturn(Optional.empty());

        IllegalArgumentException thrown = assertThrows(
                IllegalArgumentException.class,
                () -> taskService.changeDescription(nonExistingId, newDescription)
        );

        assertTrue(thrown.getMessage().contains("Task with id " + nonExistingId + " not found"));
        verify(taskRepository, times(1)).findById(nonExistingId);
        verify(taskRepository, times(0)).save(any(Task.class));
    }

    @Test
    public void testChangeDescription_whenGivenTaskObject_successfullyUpdates() {
        String newDescription = "new_description";
        when(taskRepository.save(any(Task.class))).thenAnswer(returnsFirstArg());

        Task updatedTask = taskService.changeDescription(task, newDescription);
        assertNotNull(updatedTask);
        assertEquals(newDescription, updatedTask.getDescription());

        verify(taskRepository, times(1)).save(task);
    }

    @Test
    public void testCreateTask() {
        when(taskRepository.save(any(Task.class))).thenAnswer(returnsFirstArg());
        Task newTask = taskService.createTask(task);
        assertNotNull(newTask);
        assertEquals(TEST_TITLE, newTask.getTitle());
        assertEquals(TEST_DESCRIPTION, newTask.getDescription());

        verify(taskRepository, times(1)).save(task);
    }

    @Test
    public void testDeleteTaskById_whenGivenExistingId_successfullyDeletesTask() {
        Long existingId = 1L;
        when(taskRepository.findById(existingId)).thenReturn(Optional.of(task));
        doNothing().when(taskRepository).deleteById(existingId);

        Task deletedTask = taskService.deleteTaskById(existingId);

        verify(taskRepository, times(1)).findById(existingId);
        verify(taskRepository, times(1)).deleteById(existingId);

        assertNotNull(deletedTask);
        assertEquals(task.getId(), deletedTask.getId());
        assertEquals(task.getTitle(), deletedTask.getTitle());
    }

    @Test
    public void testDeleteTaskById_whenGivenNonExistingId_throwsIllegalArgumentException() {
        Long nonExistingId = 99L;
        when(taskRepository.findById(nonExistingId)).thenReturn(Optional.empty());

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            taskService.deleteTaskById(nonExistingId);
        });

        assertEquals("Task with id " + nonExistingId + " not found", thrown.getMessage());
        verify(taskRepository, times(1)).findById(nonExistingId);
        verify(taskRepository, never()).deleteById(anyLong());
    }
}