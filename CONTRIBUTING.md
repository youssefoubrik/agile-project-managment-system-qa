# Guide de contribution - APMS

Merci de votre intérêt pour contribuer au projet APMS ! Ce document vous guidera à travers le processus de contribution.

## 📋 Table des matières

- [Code de conduite](#code-de-conduite)
- [Comment contribuer](#comment-contribuer)
- [Standards de code](#standards-de-code)
- [Conventions de nommage](#conventions-de-nommage)
- [Tests](#tests)
- [Documentation](#documentation)
- [Process de Pull Request](#process-de-pull-request)
- [Commits](#commits)

## 🤝 Code de conduite

- Soyez respectueux et inclusif
- Acceptez les critiques constructives
- Concentrez-vous sur ce qui est le mieux pour la communauté
- Faites preuve d'empathie envers les autres membres

## 🚀 Comment contribuer

### 1. Fork et Clone

```bash
# Fork le repository sur GitHub
# Puis clonez votre fork
git clone https://github.com/VOTRE-USERNAME/agile-project-managment-system-qa.git
cd agile-project-management-system-qa
```

### 2. Créer une branche

```bash
git checkout -b feature/ma-nouvelle-fonctionnalite
```

**Convention de nommage des branches :**

- `feature/description` : Nouvelles fonctionnalités
- `bugfix/description` : Corrections de bugs
- `hotfix/description` : Corrections urgentes
- `refactor/description` : Refactoring du code
- `docs/description` : Documentation uniquement
- `test/description` : Ajout ou modification de tests

### 3. Faire vos modifications

Développez votre fonctionnalité ou correction en suivant les standards de code.

### 4. Tester

```bash
# Exécuter tous les tests
./mvnw clean test

# Vérifier la couverture de code
./mvnw clean test jacoco:report
```

Assurez-vous que :

- ✅ Tous les tests passent
- ✅ La couverture de code est maintenue (minimum 80%)
- ✅ Aucun warning SonarQube critique

### 5. Commiter

```bash
git add .
git commit -m "feat: ajouter la fonctionnalité X"
```

### 6. Push et Pull Request

```bash
git push origin feature/ma-nouvelle-fonctionnalite
```

Ensuite, créez une Pull Request sur GitHub.

## 📝 Standards de code

### Structure générale

1. **Une classe = une responsabilité** (SRP)
2. **Pas de logique métier dans les contrôleurs**
3. **Pas d'accès direct aux repositories depuis les contrôleurs**
4. **Utiliser les mappers pour les conversions DTO ↔ Entité**
5. **Toujours valider les entrées**

### Conventions Java

#### Classes

```java
/**
 * Description de la classe avec son rôle et ses responsabilités.
 *
 * <p>Détails supplémentaires si nécessaire.</p>
 *
 * @author Votre Nom
 * @version 1.0
 * @since 1.0
 */
@Service
@RequiredArgsConstructor
public class MaClasse {
    // Code
}
```

#### Méthodes

```java
/**
 * Description courte de ce que fait la méthode.
 *
 * <p>Détails supplémentaires si nécessaire.</p>
 *
 * @param param1 description du paramètre
 * @param param2 description du paramètre
 * @return description du retour
 * @throws ExceptionType description de quand l'exception est levée
 */
public ReturnType methodName(ParamType param1, ParamType param2) {
    // Implémentation
}
```

#### Variables et constantes

```java
// Constantes en UPPER_SNAKE_CASE
private static final String DEFAULT_STATUS = "ACTIVE";
private static final int MAX_RETRY_ATTEMPTS = 3;

// Variables en camelCase
private UserStoryRepository userStoryRepository;
private String userName;
```

### Annotations

#### Ordre des annotations

1. Documentation (`@Deprecated`)
2. Scope et lifecycle (`@Service`, `@Component`, etc.)
3. Configuration (`@RequiredArgsConstructor`, `@Slf4j`)
4. Validation (`@Valid`, `@NotNull`)
5. Mapping (`@RequestMapping`, `@GetMapping`)
6. Transaction (`@Transactional`)
7. Custom (`@LogOperation`)

```java
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    @PostMapping
    @Transactional
    @LogOperation(description = "Create user")
    public ResponseEntity<UserResponse> create(@Valid @RequestBody UserRequest dto) {
        // Code
    }
}
```

## 🏗️ Conventions de nommage

### Packages

```
ma.ensa.apms.controller    # Contrôleurs REST
ma.ensa.apms.service       # Services métier
ma.ensa.apms.service.impl  # Implémentations
ma.ensa.apms.repository    # Repositories JPA
ma.ensa.apms.modal         # Entités
ma.ensa.apms.dto.Request   # DTOs de requête
ma.ensa.apms.dto.Response  # DTOs de réponse
```

### Classes

- **Controller** : `EntityNameController` (ex: `UserStoryController`)
- **Service Interface** : `EntityNameService` (ex: `UserStoryService`)
- **Service Impl** : `EntityNameServiceImpl` (ex: `UserStoryServiceImpl`)
- **Repository** : `EntityNameRepository` (ex: `UserStoryRepository`)
- **Entity** : `EntityName` (ex: `UserStory`)
- **Request DTO** : `EntityNameRequest` (ex: `UserStoryRequest`)
- **Response DTO** : `EntityNameResponse` (ex: `UserStoryResponse`)
- **Mapper** : `EntityNameMapper` (ex: `UserStoryMapper`)
- **Validator** : `EntityNameValidator` (ex: `UserStoryValidator`)
- **Helper** : `EntityNameRepositoryHelper` (ex: `UserStoryRepositoryHelper`)
- **Exception** : `ProblemTypeException` (ex: `ResourceNotFoundException`)

### Méthodes

#### Controllers

```java
// GET
getUserStoryById(UUID id)
getAllUserStories()
getUserStoriesByStatus(Status status)

// POST
createUserStory(UserStoryRequest dto)

// PUT
updateUserStory(UUID id, UserStoryRequest dto)

// PATCH
updateUserStoryStatus(UUID id, Status status)

// DELETE
deleteUserStory(UUID id)
```

#### Services

```java
// CRUD
create(RequestDto dto)
update(UUID id, RequestDto dto)
getById(UUID id)
getAll()
delete(UUID id)

// Business logic
changeStatus(UUID id, Status newStatus)
linkToEpic(UUID storyId, UUID epicId)
validateCanMarkAsDone(UserStory story)
```

#### Repositories

```java
// Query Methods
findById(UUID id)
findAll()
findByStatus(Status status)
findByProductBacklogId(UUID backlogId)
existsByName(String name)
countByStatus(Status status)
```

## 🧪 Tests

### Structure des tests

```java
@ExtendWith(MockitoExtension.class)
class UserStoryServiceImplTest {

    @Mock
    private UserStoryRepository repository;

    @Mock
    private UserStoryMapper mapper;

    @InjectMocks
    private UserStoryServiceImpl service;

    @Test
    @DisplayName("Should create user story successfully")
    void shouldCreateUserStorySuccessfully() {
        // Given (Arrange)
        UserStoryRequest request = createValidRequest();
        UserStory entity = createValidEntity();
        when(mapper.toEntity(request)).thenReturn(entity);
        when(repository.save(entity)).thenReturn(entity);

        // When (Act)
        UserStoryResponse response = service.create(request);

        // Then (Assert)
        assertNotNull(response);
        verify(repository).save(entity);
    }

    @Test
    @DisplayName("Should throw exception when user story not found")
    void shouldThrowExceptionWhenNotFound() {
        // Given
        UUID id = UUID.randomUUID();
        when(repository.findById(id)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(ResourceNotFoundException.class,
            () -> service.getById(id));
    }
}
```

### Couverture de code

**Minimum requis :**

- Couverture globale : 80%
- Couverture par package : 75%
- Services : 85%
- Controllers : 80%

**Ne pas tester :**

- Getters/Setters générés par Lombok
- Constructeurs générés
- Mappers MapStruct (déjà testés par le framework)

### Types de tests

1. **Tests unitaires** : Testent une unité isolée (avec mocks)
2. **Tests d'intégration** : Testent l'interaction entre composants
3. **Tests de validation** : Testent les contraintes de validation

## 📚 Documentation

### Javadoc obligatoire pour

- ✅ Classes publiques
- ✅ Interfaces
- ✅ Méthodes publiques
- ✅ Méthodes complexes
- ✅ Constantes importantes

### Javadoc optionnelle pour

- Variables privées simples
- Méthodes privées évidentes
- Tests

### Exemple de documentation complète

```java
/**
 * Service de gestion des User Stories.
 *
 * <p>Ce service fournit toutes les opérations métier relatives aux User Stories,
 * incluant la création, modification, validation et gestion du cycle de vie.</p>
 *
 * <p><b>Règles métier appliquées :</b></p>
 * <ul>
 *   <li>Une User Story ne peut être marquée DONE que si tous ses critères sont satisfaits</li>
 *   <li>Une User Story ne peut être liée à un Epic que si elle est en statut TODO</li>
 *   <li>Le déplacement vers un Sprint retire la story du Product Backlog</li>
 * </ul>
 *
 * @author APMS Team
 * @version 1.0
 * @since 1.0
 * @see UserStory
 * @see UserStoryRequest
 * @see UserStoryResponse
 */
@Service
@RequiredArgsConstructor
public class UserStoryServiceImpl implements UserStoryService {

    /**
     * Crée une nouvelle User Story dans le système.
     *
     * <p>La User Story est initialisée avec le statut TODO et est ajoutée
     * au Product Backlog spécifié dans la requête.</p>
     *
     * @param dto les données de la User Story à créer (ne doit pas être null)
     * @return la User Story créée avec son identifiant généré
     * @throws ResourceNotFoundException si le Product Backlog référencé n'existe pas
     * @throws IllegalArgumentException si le DTO est null ou invalide
     */
    @Override
    @Transactional
    @LogOperation(description = "Creating new user story")
    public UserStoryResponse create(UserStoryRequest dto) {
        // Implémentation
    }
}
```

## 🔄 Process de Pull Request

### Avant de soumettre

- [ ] Les tests passent tous
- [ ] La couverture de code est maintenue
- [ ] Le code est documenté
- [ ] Le code suit les conventions
- [ ] Pas de TODO ou FIXME non résolus
- [ ] Les dépendances sont à jour
- [ ] La branche est à jour avec main

### Template de Pull Request

```markdown
## Description

Brève description des changements

## Type de changement

- [ ] Bug fix
- [ ] Nouvelle fonctionnalité
- [ ] Breaking change
- [ ] Documentation

## Tests

- [ ] Tests unitaires ajoutés/modifiés
- [ ] Tests d'intégration ajoutés/modifiés
- [ ] Tous les tests passent

## Checklist

- [ ] Code suit les conventions
- [ ] Documentation ajoutée/mise à jour
- [ ] Pas de warnings de compilation
- [ ] Couverture de code maintenue
```

### Review

Les reviews porteront sur :

1. **Fonctionnalité** : Le code fait-il ce qu'il doit faire ?
2. **Tests** : Les tests couvrent-ils les cas limites ?
3. **Qualité** : Le code est-il maintenable et lisible ?
4. **Performance** : Y a-t-il des problèmes de performance potentiels ?
5. **Sécurité** : Y a-t-il des vulnérabilités ?

## 📝 Commits

### Format des messages de commit

Suivre la convention **Conventional Commits** :

```
<type>(<scope>): <subject>

<body>

<footer>
```

#### Types

- `feat` : Nouvelle fonctionnalité
- `fix` : Correction de bug
- `docs` : Documentation uniquement
- `style` : Formatage, point-virgule manquant, etc.
- `refactor` : Refactoring du code
- `test` : Ajout/modification de tests
- `chore` : Maintenance, dépendances, etc.
- `perf` : Amélioration de performance
- `ci` : Configuration CI/CD

#### Exemples

```bash
feat(user-story): add ability to link story to epic

Implement the linkToEpic method in UserStoryService.
Add validation to ensure story is in TODO status.

Closes #123
```

```bash
fix(validation): correct date validation logic

The end date was not properly validated against start date.
Added unit tests to cover this scenario.

Fixes #456
```

```bash
docs(readme): update installation instructions

Add Docker Compose setup instructions.
Clarify database configuration steps.
```

## 🐛 Signaler un bug

Utilisez le template d'issue sur GitHub :

```markdown
**Description du bug**
Description claire et concise du bug

**Comment reproduire**

1. Aller sur '...'
2. Cliquer sur '....'
3. Voir l'erreur

**Comportement attendu**
Ce qui devrait se passer

**Screenshots**
Si applicable

**Environnement**

- OS: [ex: Ubuntu 20.04]
- Java: [ex: 17]
- Version APMS: [ex: 0.0.1-SNAPSHOT]

**Logs**
```

Coller les logs pertinents ici

```

```

## 💡 Proposer une fonctionnalité

Utilisez le template d'issue sur GitHub :

```markdown
**Est-ce lié à un problème ?**
Description claire du problème

**Solution proposée**
Description de la solution souhaitée

**Alternatives considérées**
Autres solutions envisagées

**Contexte additionnel**
Tout contexte ou screenshot utile
```

## 📞 Questions ?

- Ouvrir une issue avec le tag `question`
- Consulter la documentation existante
- Regarder les issues fermées

---

**Merci de contribuer à APMS ! 🎉**
