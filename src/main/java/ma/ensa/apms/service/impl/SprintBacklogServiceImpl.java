package ma.ensa.apms.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import ma.ensa.apms.dto.Request.SprintBacklogRequest;
import ma.ensa.apms.dto.Request.UserStoryRequest;
import ma.ensa.apms.dto.Response.SprintBacklogResponse;
import ma.ensa.apms.dto.Response.UserStoryResponse;
import ma.ensa.apms.mapper.SprintBacklogMapper;
import ma.ensa.apms.mapper.UserStoryMapper;
import ma.ensa.apms.modal.SprintBacklog;
import ma.ensa.apms.modal.UserStory;
import ma.ensa.apms.repository.SprintBacklogRepository;
import ma.ensa.apms.repository.UserStoryRepository;
import ma.ensa.apms.service.SprintBacklogService;
import ma.ensa.apms.service.helper.SprintBacklogRepositoryHelper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SprintBacklogServiceImpl implements SprintBacklogService {

    private final SprintBacklogRepository sprintBacklogRepository;
    private final SprintBacklogMapper sprintBacklogMapper;
    private final UserStoryMapper userStoryMapper;
    private final UserStoryRepository userStoryRepository;
    private final SprintBacklogRepositoryHelper sprintBacklogRepositoryHelper;

    @Override
    @Transactional
    public SprintBacklogResponse createSprintBacklog(SprintBacklogRequest request) {
        SprintBacklog sprintBacklog = sprintBacklogMapper.toEntity(request);
        SprintBacklog savedSprintBacklog = sprintBacklogRepository.save(sprintBacklog);
        return sprintBacklogMapper.toResponse(savedSprintBacklog);
    }

    @Override
    public SprintBacklogResponse getSprintBacklogById(UUID id) {
        SprintBacklog sprintBacklog = sprintBacklogRepositoryHelper.findByIdOrThrow(id);
        return sprintBacklogMapper.toResponse(sprintBacklog);
    }

    @Override
    public List<SprintBacklogResponse> getAllSprintBacklogs() {
        return sprintBacklogRepository.findAll().stream()
                .map(sprintBacklogMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public SprintBacklogResponse updateSprintBacklog(UUID id, SprintBacklogRequest request) {
        SprintBacklog sprintBacklog = sprintBacklogRepositoryHelper.findByIdOrThrow(id);

        sprintBacklogMapper.updateFromDto(request, sprintBacklog);
        SprintBacklog updatedSprintBacklog = sprintBacklogRepository.save(sprintBacklog);
        return sprintBacklogMapper.toResponse(updatedSprintBacklog);
    }

    @Override
    @Transactional
    public void deleteSprintBacklog(UUID id) {
        sprintBacklogRepositoryHelper.validateExists(id);
        sprintBacklogRepository.deleteById(id);
    }

    @Override
    @Transactional
    public List<UserStoryResponse> getUserStoriesBySprintBacklogId(UUID sprintBacklogId) {
        SprintBacklog sprintBacklog = sprintBacklogRepositoryHelper.findByIdOrThrow(sprintBacklogId);

        return sprintBacklog.getUserStories().stream()
                .map(userStoryMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public UserStoryResponse addUserStoryToSprintBacklog(UUID sprintBacklogId, UserStoryRequest userStoryRequest) {
        SprintBacklog sprintBacklog = sprintBacklogRepositoryHelper.findByIdOrThrow(sprintBacklogId);

        UserStory userStory = userStoryMapper.toEntity(userStoryRequest);
        userStory.setSprintBacklog(sprintBacklog);
        UserStory savedUserStory = userStoryRepository.save(userStory);

        return userStoryMapper.toResponse(savedUserStory);
    }

    @Override
    @Transactional
    public void removeUserStoryFromSprintBacklog(UUID sprintBacklogId, UUID userStoryId) {
        sprintBacklogRepositoryHelper.validateExists(sprintBacklogId);

        UserStory userStory = sprintBacklogRepositoryHelper.findUserStoryByIdOrThrow(userStoryId);

        if (!userStory.getSprintBacklog().getId().equals(sprintBacklogId)) {
            throw new IllegalArgumentException("User story does not belong to the specified sprint backlog");
        }

        userStoryRepository.delete(userStory);
    }

}