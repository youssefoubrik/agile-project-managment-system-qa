package ma.ensa.apms.service.helper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import jakarta.persistence.EntityNotFoundException;
import ma.ensa.apms.modal.Project;
import ma.ensa.apms.repository.ProjectRepository;

/**
 * Unit tests for {@link ProjectRepositoryHelper}
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("ProjectRepositoryHelper Tests")
class ProjectRepositoryHelperTest {

    @Mock
    private ProjectRepository projectRepository;

    @InjectMocks
    private ProjectRepositoryHelper projectRepositoryHelper;

    private UUID projectId;
    private Project project;

    @BeforeEach
    void setUp() {
        projectId = UUID.randomUUID();
        project = new Project();
        project.setId(projectId);
        project.setName("Test Project");
    }

    @Test
    @DisplayName("findByIdOrThrow should return project when found")
    void findByIdOrThrow_WhenProjectExists_ShouldReturnProject() {
        // Given
        when(projectRepository.findById(projectId)).thenReturn(Optional.of(project));

        // When
        Project result = projectRepositoryHelper.findByIdOrThrow(projectId);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(projectId);
        assertThat(result.getName()).isEqualTo("Test Project");
        verify(projectRepository).findById(projectId);
    }

    @Test
    @DisplayName("findByIdOrThrow should throw EntityNotFoundException when project not found")
    void findByIdOrThrow_WhenProjectNotFound_ShouldThrowException() {
        // Given
        when(projectRepository.findById(projectId)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> projectRepositoryHelper.findByIdOrThrow(projectId))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Project not found");
        verify(projectRepository).findById(projectId);
    }

    @Test
    @DisplayName("findByIdOrThrow should work with different project IDs")
    void findByIdOrThrow_WithDifferentIds_ShouldWorkCorrectly() {
        // Given
        UUID anotherId = UUID.randomUUID();
        Project anotherProject = new Project();
        anotherProject.setId(anotherId);
        anotherProject.setName("Another Project");
        when(projectRepository.findById(anotherId)).thenReturn(Optional.of(anotherProject));

        // When
        Project result = projectRepositoryHelper.findByIdOrThrow(anotherId);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(anotherId);
        assertThat(result.getName()).isEqualTo("Another Project");
        verify(projectRepository).findById(anotherId);
    }
}
