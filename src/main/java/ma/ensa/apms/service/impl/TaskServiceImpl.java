package ma.ensa.apms.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import ma.ensa.apms.dto.TaskEndDateUpdateDto;
import ma.ensa.apms.dto.TaskRequestDto;
import ma.ensa.apms.dto.TaskResponseDto;
import ma.ensa.apms.dto.TaskStartDateUpdateDto;
import ma.ensa.apms.dto.TaskStatusUpdateDto;
import ma.ensa.apms.mapper.TaskMapper;
import ma.ensa.apms.modal.Task;
import ma.ensa.apms.modal.enums.TaskStatus;
import ma.ensa.apms.repository.TaskRepository;
import ma.ensa.apms.service.TaskService;
import ma.ensa.apms.service.helper.TaskRepositoryHelper;
import ma.ensa.apms.service.validator.TaskDateValidator;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;
    private final TaskRepositoryHelper taskRepositoryHelper;
    private final TaskDateValidator taskDateValidator;

    @Override
    @Transactional
    public TaskResponseDto createTask(TaskRequestDto taskDto) {
        Task task = taskMapper.toEntity(taskDto);
        Task savedTask = taskRepository.save(task);
        return taskMapper.toDto(savedTask);
    }

    @Override
    public List<TaskResponseDto> getAllTasks() {
        return taskRepository.findAll()
                .stream()
                .map(taskMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public TaskResponseDto getTaskById(UUID id) {
        Task task = taskRepositoryHelper.findByIdOrThrow(id);
        return taskMapper.toDto(task);
    }

    @Override
    public List<TaskResponseDto> getTasksByStatus(TaskStatus status) {
        return taskRepository.findByStatus(status)
                .stream()
                .map(taskMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<TaskResponseDto> getTasksByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return taskRepository.findByStartDateGreaterThanEqualAndEndDateLessThanEqual(startDate, endDate)
                .stream()
                .map(taskMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public TaskResponseDto updateTask(UUID id, TaskRequestDto taskDto) {
        Task existingTask = taskRepositoryHelper.findByIdOrThrow(id);

        Task updatedTask = taskMapper.toEntity(taskDto);
        updatedTask.setId(existingTask.getId());

        return taskMapper.toDto(taskRepository.save(updatedTask));
    }

    @Override
    @Transactional
    public TaskResponseDto updateTaskStatus(UUID id, TaskStatusUpdateDto statusDto) {
        Task task = taskRepositoryHelper.findByIdOrThrow(id);

        task.setStatus(statusDto.getStatus());
        Task updatedTask = taskRepository.save(task);
        return taskMapper.toDto(updatedTask);
    }

    @Override
    @Transactional
    public TaskResponseDto updateTaskStartDate(UUID id, TaskStartDateUpdateDto startDateDto) {
        Task task = taskRepositoryHelper.findByIdOrThrow(id);

        LocalDateTime newStartDate = startDateDto.getStartDate();
        taskDateValidator.validateStartDate(newStartDate, task.getEndDate());

        task.setStartDate(newStartDate);
        Task updatedTask = taskRepository.save(task);
        return taskMapper.toDto(updatedTask);
    }

    @Override
    @Transactional
    public TaskResponseDto updateTaskEndDate(UUID id, TaskEndDateUpdateDto endDateDto) {
        Task task = taskRepositoryHelper.findByIdOrThrow(id);

        LocalDateTime newEndDate = endDateDto.getEndDate();
        taskDateValidator.validateEndDate(task.getStartDate(), newEndDate);

        task.setEndDate(newEndDate);
        Task updatedTask = taskRepository.save(task);
        return taskMapper.toDto(updatedTask);
    }

    @Override
    @Transactional
    public void deleteTask(UUID id) {
        Task task = taskRepositoryHelper.findByIdOrThrow(id);
        taskRepository.delete(task);
    }
}
