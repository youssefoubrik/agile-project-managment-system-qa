package ma.ensa.apms.service.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import ma.ensa.apms.dto.Request.EpicRequest;
import ma.ensa.apms.dto.Response.EpicResponse;
import ma.ensa.apms.dto.Response.ProductBacklogResponse;
import ma.ensa.apms.dto.Response.UserStoryResponse;
import ma.ensa.apms.exception.ResourceNotFoundException;
import ma.ensa.apms.mapper.EpicMapper;
import ma.ensa.apms.mapper.ProductBacklogMapper;
import ma.ensa.apms.mapper.UserStoryMapper;
import ma.ensa.apms.modal.Epic;
import ma.ensa.apms.modal.UserStory;
import ma.ensa.apms.repository.EpicRepository;
import ma.ensa.apms.repository.UserStoryRepository;
import ma.ensa.apms.service.EpicService;
import ma.ensa.apms.service.helper.EpicRepositoryHelper;

@Service
@RequiredArgsConstructor
public class EpicServiceImpl implements EpicService {

    private final EpicRepository epicRepository;
    private final UserStoryRepository userStoryRepository;
    private final EpicMapper epicMapper;
    private final UserStoryMapper userStoryMapper;
    private final ProductBacklogMapper productBacklogMapper;
    private final EpicRepositoryHelper epicRepositoryHelper;

    @Override
    @Transactional
    public EpicResponse create(EpicRequest dto) {
        Epic epic = epicMapper.toEntity(dto);
        return epicMapper.toDto(epicRepository.save(epic));
    }

    @Override
    public EpicResponse findById(UUID id) {
        Epic epic = epicRepositoryHelper.findByIdOrThrow(id);
        EpicResponse response = epicMapper.toDto(epic);
        response.setUserStoriesCount(epicRepositoryHelper.getUserStoriesCount(epic));
        return response;
    }

    @Override
    public List<EpicResponse> findAll() {
        return epicRepository.findAll().stream()
                .map(epic -> {
                    EpicResponse response = epicMapper.toDto(epic);
                    response.setUserStoriesCount(epicRepositoryHelper.getUserStoriesCount(epic));
                    return response;
                })
                .toList();
    }

    @Override
    @Transactional
    public EpicResponse update(UUID id, EpicRequest dto) {
        Epic epic = epicRepositoryHelper.findByIdOrThrow(id);
        epicMapper.updateEntityFromDto(dto, epic);
        return epicMapper.toDto(epicRepository.save(epic));
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        Epic epic = epicRepositoryHelper.findByIdOrThrow(id);
        epicRepository.delete(epic);
    }

    @Override
    @Transactional
    public EpicResponse addUserStoryToEpic(UUID epicId, UUID userStoryId) {
        Epic epic = epicRepositoryHelper.findByIdOrThrow(epicId);
        UserStory userStory = epicRepositoryHelper.findUserStoryByIdOrThrow(userStoryId);

        userStory.setEpic(epic);
        userStoryRepository.save(userStory);

        return epicMapper.toDto(epic);
    }

    @Override
    public List<UserStoryResponse> getUserStoriesByEpicId(UUID epicId) {
        Epic epic = epicRepositoryHelper.findByIdOrThrow(epicId);
        return epic.getUserStories().stream()
                .map(userStoryMapper::toResponse)
                .toList();
    }

    @Override
    public ProductBacklogResponse getProductBacklogByEpicId(UUID epicId) {
        Epic epic = epicRepositoryHelper.findByIdOrThrow(epicId);
        if (epic.getProductBacklog() == null) {
            throw new ResourceNotFoundException("No product backlog is associated with epic id: " + epicId);
        }
        return productBacklogMapper.toResponse(epic.getProductBacklog());
    }

    @Override
    public long countEpics() {
        return epicRepository.count();
    }
}
