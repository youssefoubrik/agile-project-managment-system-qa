package ma.ensa.apms.service.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import ma.ensa.apms.dto.Request.EpicRequest;
import ma.ensa.apms.dto.Request.ProductBacklogRequest;
import ma.ensa.apms.dto.Request.UserStoryRequest;
import ma.ensa.apms.dto.Response.EpicResponse;
import ma.ensa.apms.dto.Response.ProductBacklogResponse;
import ma.ensa.apms.dto.Response.ProjectResponse;
import ma.ensa.apms.dto.Response.UserStoryResponse;
import ma.ensa.apms.mapper.EpicMapper;
import ma.ensa.apms.mapper.ProductBacklogMapper;
import ma.ensa.apms.mapper.ProjectMapper;
import ma.ensa.apms.mapper.UserStoryMapper;
import ma.ensa.apms.modal.Epic;
import ma.ensa.apms.modal.ProductBacklog;
import ma.ensa.apms.modal.Project;
import ma.ensa.apms.modal.UserStory;
import ma.ensa.apms.repository.EpicRepository;
import ma.ensa.apms.repository.ProductBacklogRepository;
import ma.ensa.apms.repository.UserStoryRepository;
import ma.ensa.apms.service.ProductBacklogService;
import ma.ensa.apms.service.helper.ProductBacklogRepositoryHelper;
import ma.ensa.apms.service.validator.ProductBacklogValidator;

@Service
@RequiredArgsConstructor
public class ProductBacklogServiceImpl implements ProductBacklogService {

    private final UserStoryRepository userStoryRepository;
    private final UserStoryMapper userStoryMapper;
    private final ProductBacklogRepository productBacklogRepository;
    private final ProductBacklogMapper productBacklogMapper;
    private final EpicMapper epicMapper;
    private final EpicRepository epicRepository;
    private final ProjectMapper projectMapper;
    private final ProductBacklogRepositoryHelper productBacklogRepositoryHelper;
    private final ProductBacklogValidator productBacklogValidator;

    @Override
    @Transactional
    public ProductBacklogResponse create(ProductBacklogRequest request) {
        ProductBacklog pb = productBacklogMapper.toEntity(request);
        productBacklogRepository.save(pb);
        return productBacklogMapper.toResponse(pb);
    }

    @Override
    public ProductBacklogResponse getProductBacklogById(UUID id) {
        ProductBacklog pb = productBacklogRepositoryHelper.findByIdOrThrow(id);
        ProductBacklogResponse response = productBacklogMapper.toResponse(pb);
        setCounts(response, pb);
        return response;
    }

    @Override
    @Transactional
    public void deleteProductBacklog(UUID productBacklogId) {
        productBacklogRepositoryHelper.validateExists(productBacklogId);
        productBacklogRepository.deleteById(productBacklogId);
    }

    @Override
    public List<ProductBacklogResponse> getAllProductBacklogs() {
        return productBacklogRepository.findAll().stream()
                .map(productBacklogMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public List<UserStoryResponse> getUserStoriesByProductBacklogId(UUID productBacklogId) {
        productBacklogRepositoryHelper.validateExists(productBacklogId);
        return userStoryRepository.findByProductBacklogId(productBacklogId).stream()
                .map(userStoryMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public List<EpicResponse> getEpicsByProductBacklogId(UUID productBacklogId) {
        ProductBacklog productBacklog = productBacklogRepositoryHelper.findByIdOrThrow(productBacklogId);
        return productBacklog.getEpics().stream()
                .map(epicMapper::toDto)
                .toList();
    }

    private void setCounts(ProductBacklogResponse response, ProductBacklog pb) {
        response.setUserStoryCount(pb.getUserStories() != null ? pb.getUserStories().size() : 0);
        response.setEpicCount(pb.getEpics() != null ? pb.getEpics().size() : 0);
    }

    @Override
    @Transactional
    public EpicResponse addEpicToProductBacklog(UUID productBacklogId, EpicRequest epicRequest) {
        ProductBacklog productBacklog = productBacklogRepositoryHelper.findByIdOrThrow(productBacklogId);

        Epic epic = epicMapper.toEntity(epicRequest);
        epic.setProductBacklog(productBacklog);
        Epic savedEpic = epicRepository.save(epic);
        productBacklog.getEpics().add(savedEpic);

        productBacklogRepository.save(productBacklog);
        return epicMapper.toDto(savedEpic);
    }

    @Override
    @Transactional
    public UserStoryResponse addUserStoryToProductBacklog(UUID productBacklogId, UserStoryRequest userStoryRequest) {
        ProductBacklog productBacklog = productBacklogRepositoryHelper.findByIdOrThrow(productBacklogId);

        UserStory userStory = userStoryMapper.toEntity(userStoryRequest);
        userStory.setProductBacklog(productBacklog);
        productBacklog.getUserStories().add(userStory);

        productBacklogRepository.save(productBacklog);
        return userStoryMapper.toResponse(userStory);
    }

    @Override
    @Transactional
    public ProjectResponse getProjectByProductBacklogId(UUID productBacklogId) {
        ProductBacklog productBacklog = productBacklogRepositoryHelper.findByIdOrThrow(productBacklogId);
        productBacklogValidator.validateHasProject(productBacklog);

        Project project = productBacklog.getProject();
        return projectMapper.toResponse(project);
    }

}
