package ma.ensa.apms.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ma.ensa.apms.dto.TaskEndDateUpdateDto;
import ma.ensa.apms.dto.TaskRequestDto;
import ma.ensa.apms.dto.TaskResponseDto;
import ma.ensa.apms.dto.TaskStartDateUpdateDto;
import ma.ensa.apms.dto.TaskStatusUpdateDto;
import ma.ensa.apms.exception.BusinessException;
import ma.ensa.apms.exception.ResourceNotFoundException;
import ma.ensa.apms.mapper.TaskMapper;
import ma.ensa.apms.modal.Task;
import ma.ensa.apms.modal.enums.TaskStatus;
import ma.ensa.apms.repository.TaskRepository;

@ExtendWith(MockitoExtension.class)
class TaskServiceImplTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private TaskMapper taskMapper;

    @Mock
    private ma.ensa.apms.service.helper.TaskRepositoryHelper taskRepositoryHelper;

    @Mock
    private ma.ensa.apms.service.validator.TaskDateValidator taskDateValidator;

    @InjectMocks
    private TaskServiceImpl taskService;

    private UUID taskId;
    private Task task;
    private TaskResponseDto taskResponseDto;
    private TaskRequestDto taskRequestDto;
    private LocalDateTime startDate;
    private LocalDateTime endDate;

    @BeforeEach
    void setUp() {
        taskId = UUID.randomUUID();
        startDate = LocalDateTime.now();
        endDate = startDate.plusDays(7);

        task = Task.builder()
                .id(taskId)
                .title("Test Task")
                .description("Description for test task")
                .status(TaskStatus.TODO)
                .startDate(startDate)
                .endDate(endDate)
                .build();

        taskResponseDto = new TaskResponseDto();
        taskResponseDto.setId(taskId);
        taskResponseDto.setTitle("Test Task");
        taskResponseDto.setDescription("Description for test task");
        taskResponseDto.setStatus(TaskStatus.TODO);
        taskResponseDto.setStartDate(startDate);
        taskResponseDto.setEndDate(endDate);

        taskRequestDto = new TaskRequestDto();
        taskRequestDto.setTitle("Test Task");
        taskRequestDto.setDescription("Description for test task");
        taskRequestDto.setStatus(TaskStatus.TODO);
        taskRequestDto.setStartDate(startDate);
        taskRequestDto.setEndDate(endDate);
    }

    @Test
    void createTask_ShouldReturnCreatedTask() {
        // Arrange
        when(taskMapper.toEntity(any(TaskRequestDto.class))).thenReturn(task);
        when(taskRepository.save(any(Task.class))).thenReturn(task);
        when(taskMapper.toDto(any(Task.class))).thenReturn(taskResponseDto);

        // Act
        TaskResponseDto result = taskService.createTask(taskRequestDto);

        // Assert
        assertNotNull(result);
        assertEquals(taskId, result.getId());
        assertEquals("Test Task", result.getTitle());
        verify(taskRepository).save(task);
    }

    @Test
    void getAllTasks_ShouldReturnAllTasks() {
        // Arrange
        Task task2 = Task.builder().id(UUID.randomUUID()).title("Second Task").build();
        List<Task> tasks = Arrays.asList(task, task2);

        TaskResponseDto dto2 = new TaskResponseDto();
        dto2.setId(task2.getId());
        dto2.setTitle("Second Task");

        when(taskRepository.findAll()).thenReturn(tasks);
        when(taskMapper.toDto(task)).thenReturn(taskResponseDto);
        when(taskMapper.toDto(task2)).thenReturn(dto2);

        // Act
        List<TaskResponseDto> result = taskService.getAllTasks();

        // Assert
        assertEquals(2, result.size());
        assertEquals("Test Task", result.get(0).getTitle());
        assertEquals("Second Task", result.get(1).getTitle());
    }

    @Test
    void getTaskById_WhenTaskExists_ShouldReturnTask() {
        // Arrange
        when(taskRepositoryHelper.findByIdOrThrow(taskId)).thenReturn(task);
        when(taskMapper.toDto(task)).thenReturn(taskResponseDto);

        // Act
        TaskResponseDto result = taskService.getTaskById(taskId);

        // Assert
        assertNotNull(result);
        assertEquals(taskId, result.getId());
    }

    @Test
    void getTaskById_WhenTaskDoesNotExist_ShouldThrowException() {
        // Arrange
        when(taskRepositoryHelper.findByIdOrThrow(taskId))
                .thenThrow(new ResourceNotFoundException("Task not found with id: " + taskId));

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> taskService.getTaskById(taskId));
    }

    @Test
    void getTasksByStatus_ShouldReturnTasksWithSpecifiedStatus() {
        // Arrange
        List<Task> tasks = Arrays.asList(task);
        when(taskRepository.findByStatus(TaskStatus.TODO)).thenReturn(tasks);
        when(taskMapper.toDto(task)).thenReturn(taskResponseDto);

        // Act
        List<TaskResponseDto> result = taskService.getTasksByStatus(TaskStatus.TODO);

        // Assert
        assertEquals(1, result.size());
        assertEquals(TaskStatus.TODO, result.get(0).getStatus());
    }

    @Test
    void getTasksByDateRange_ShouldReturnTasksInRange() {
        // Arrange
        List<Task> tasks = Arrays.asList(task);
        when(taskRepository.findByStartDateGreaterThanEqualAndEndDateLessThanEqual(startDate, endDate))
                .thenReturn(tasks);
        when(taskMapper.toDto(task)).thenReturn(taskResponseDto);

        // Act
        List<TaskResponseDto> result = taskService.getTasksByDateRange(startDate, endDate);

        // Assert
        assertEquals(1, result.size());
    }

    @Test
    void updateTask_WhenTaskExists_ShouldUpdateTask() {
        // Arrange
        when(taskRepositoryHelper.findByIdOrThrow(taskId)).thenReturn(task);
        when(taskMapper.toEntity(taskRequestDto)).thenReturn(task);
        when(taskRepository.save(task)).thenReturn(task);
        when(taskMapper.toDto(task)).thenReturn(taskResponseDto);

        // Act
        TaskResponseDto result = taskService.updateTask(taskId, taskRequestDto);

        // Assert
        assertNotNull(result);
        assertEquals(taskId, result.getId());
    }

    @Test
    void updateTask_WhenTaskDoesNotExist_ShouldThrowException() {
        // Arrange
        when(taskRepositoryHelper.findByIdOrThrow(taskId))
                .thenThrow(new ResourceNotFoundException("Task not found with id: " + taskId));

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> taskService.updateTask(taskId, taskRequestDto));
    }

    @Test
    void updateTaskStatus_WhenTaskExists_ShouldUpdateStatus() {
        // Arrange
        TaskStatusUpdateDto statusDto = new TaskStatusUpdateDto();
        statusDto.setStatus(TaskStatus.IN_PROGRESS);

        when(taskRepositoryHelper.findByIdOrThrow(taskId)).thenReturn(task);
        when(taskRepository.save(task)).thenReturn(task);
        when(taskMapper.toDto(task)).thenReturn(taskResponseDto);

        // Act
        taskService.updateTaskStatus(taskId, statusDto);

        // Assert
        verify(taskRepository).save(task);
        assertEquals(TaskStatus.IN_PROGRESS, task.getStatus());
    }

    @Test
    void updateTaskStartDate_WhenStartDateIsValid_ShouldUpdateStartDate() {
        // Arrange
        TaskStartDateUpdateDto startDateDto = new TaskStartDateUpdateDto();
        LocalDateTime newStartDate = startDate.minusDays(1);
        startDateDto.setStartDate(newStartDate);

        when(taskRepositoryHelper.findByIdOrThrow(taskId)).thenReturn(task);
        doNothing().when(taskDateValidator).validateStartDate(newStartDate, task.getEndDate());
        when(taskRepository.save(task)).thenReturn(task);
        when(taskMapper.toDto(task)).thenReturn(taskResponseDto);

        // Act
        TaskResponseDto result = taskService.updateTaskStartDate(taskId, startDateDto);

        // Assert
        assertNotNull(result);
        verify(taskRepository).save(task);
        assertEquals(newStartDate, task.getStartDate());
    }

    @Test
    void updateTaskStartDate_WhenStartDateIsAfterEndDate_ShouldThrowException() {
        // Arrange
        TaskStartDateUpdateDto startDateDto = new TaskStartDateUpdateDto();
        LocalDateTime invalidStartDate = endDate.plusDays(1);
        startDateDto.setStartDate(invalidStartDate);

        when(taskRepositoryHelper.findByIdOrThrow(taskId)).thenReturn(task);
        doThrow(new BusinessException("Start date cannot be after the end date"))
                .when(taskDateValidator).validateStartDate(invalidStartDate, task.getEndDate());

        // Act & Assert
        assertThrows(BusinessException.class, () -> taskService.updateTaskStartDate(taskId, startDateDto));
    }

    @Test
    void updateTaskEndDate_WhenEndDateIsValid_ShouldUpdateEndDate() {
        // Arrange
        TaskEndDateUpdateDto endDateDto = new TaskEndDateUpdateDto();
        LocalDateTime newEndDate = endDate.plusDays(1);
        endDateDto.setEndDate(newEndDate);

        when(taskRepositoryHelper.findByIdOrThrow(taskId)).thenReturn(task);
        doNothing().when(taskDateValidator).validateEndDate(task.getStartDate(), newEndDate);
        when(taskRepository.save(task)).thenReturn(task);
        when(taskMapper.toDto(task)).thenReturn(taskResponseDto);

        // Act
        TaskResponseDto result = taskService.updateTaskEndDate(taskId, endDateDto);

        // Assert
        assertNotNull(result);
        verify(taskRepository).save(task);
        assertEquals(newEndDate, task.getEndDate());
    }

    @Test
    void updateTaskEndDate_WhenEndDateIsBeforeStartDate_ShouldThrowException() {
        // Arrange
        TaskEndDateUpdateDto endDateDto = new TaskEndDateUpdateDto();
        LocalDateTime invalidEndDate = startDate.minusDays(1);
        endDateDto.setEndDate(invalidEndDate);

        when(taskRepositoryHelper.findByIdOrThrow(taskId)).thenReturn(task);
        doThrow(new BusinessException("End date cannot be before the start date"))
                .when(taskDateValidator).validateEndDate(task.getStartDate(), invalidEndDate);

        // Act & Assert
        assertThrows(BusinessException.class, () -> taskService.updateTaskEndDate(taskId, endDateDto));
    }

    @Test
    void deleteTask_WhenTaskExists_ShouldDeleteTask() {
        // Arrange
        when(taskRepositoryHelper.findByIdOrThrow(taskId)).thenReturn(task);
        doNothing().when(taskRepository).delete(task);

        // Act
        taskService.deleteTask(taskId);

        // Assert
        verify(taskRepository).delete(task);
    }

    @Test
    void deleteTask_WhenTaskDoesNotExist_ShouldThrowException() {
        // Arrange
        when(taskRepositoryHelper.findByIdOrThrow(taskId))
                .thenThrow(new ResourceNotFoundException("Task not found with id: " + taskId));

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> taskService.deleteTask(taskId));
    }
}
