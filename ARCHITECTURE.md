# Architecture du Système APMS

## Vue d'ensemble

L'Agile Project Management System (APMS) est conçu selon une architecture en couches (Layered Architecture) respectant les principes de séparation des responsabilités et les bonnes pratiques Spring Boot.

## Architecture en couches

### 1. Couche Présentation (Controllers)

**Responsabilités :**

- Exposition des endpoints REST API
- Validation des requêtes entrantes
- Gestion des codes HTTP
- Transformation des exceptions en réponses HTTP appropriées

**Composants :**

- `ProjectController`
- `EpicController`
- `UserStoryController`
- `TaskController`
- `AcceptanceCriteriaController`
- `ProductBacklogController`
- `SprintBacklogController`

**Annotations clés :**

- `@RestController` : Marque la classe comme contrôleur REST
- `@RequestMapping` : Définit le préfixe de route
- `@Valid` : Active la validation des DTOs
- `@LogOperation` : Annotation personnalisée pour le logging AOP

### 2. Couche Service (Business Logic)

**Responsabilités :**

- Implémentation de la logique métier
- Orchestration des opérations complexes
- Application des règles de validation métier
- Gestion des transactions

**Organisation :**

```
service/
├── interfaces/          # Contrats de services
│   ├── ProjectService
│   ├── UserStoryService
│   └── ...
├── impl/               # Implémentations
│   ├── ProjectServiceImpl
│   ├── UserStoryServiceImpl
│   └── ...
├── validator/          # Validateurs métier
│   ├── ProjectValidator
│   ├── UserStoryValidator
│   └── ...
└── helper/            # Helpers de récupération
    ├── ProjectRepositoryHelper
    ├── UserStoryRepositoryHelper
    └── ...
```

**Patterns utilisés :**

- **Strategy Pattern** : Validateurs interchangeables
- **Helper Pattern** : Centralisation de la logique de récupération
- **Dependency Injection** : Via constructeur avec Lombok `@RequiredArgsConstructor`

### 3. Couche Mappers (Transformation)

**Responsabilités :**

- Conversion Entité ↔ DTO
- Isolation du modèle de domaine
- Réduction du couplage

**Technologie :** MapStruct (génération de code à la compilation)

**Exemple :**

```java
@Mapper(componentModel = "spring")
public interface UserStoryMapper {
    UserStoryResponse toResponse(UserStory entity);
    UserStory toEntity(UserStoryRequest dto);
    void updateEntityFromDto(UserStoryRequest dto, @MappingTarget UserStory entity);
}
```

### 4. Couche Persistance (Repositories)

**Responsabilités :**

- Accès aux données
- Requêtes JPQL/SQL
- Gestion du cache de premier niveau

**Technologie :** Spring Data JPA

**Méthodes disponibles :**

- CRUD de base via `JpaRepository`
- Query Methods dérivées du nom
- Requêtes JPQL avec `@Query`

### 5. Couche Domaine (Entities)

**Responsabilités :**

- Représentation du modèle métier
- Définition des relations
- Contraintes d'intégrité

**Hiérarchie :**

```
BaseEntity (abstract)
├── Project
├── ProductBacklog
├── Epic
├── SprintBacklog
└── (UserStory, Task, AcceptanceCriteria n'héritent pas de BaseEntity)
```

**Relations JPA :**

- `@OneToOne` : Project ↔ ProductBacklog
- `@OneToMany` : ProductBacklog → UserStories, Epics, SprintBacklogs
- `@ManyToOne` : UserStory → ProductBacklog, Epic, SprintBacklog
- `@OneToMany` : UserStory → AcceptanceCriteria, Tasks

## Patterns et Principes

### Patterns de conception

1. **Repository Pattern**

   - Abstraction de l'accès aux données
   - Fourni par Spring Data JPA

2. **DTO Pattern**

   - Séparation entre modèle interne et API
   - Protection du modèle de domaine

3. **Service Layer Pattern**

   - Centralisation de la logique métier
   - Gestion transactionnelle

4. **Validator Pattern**

   - Séparation des règles de validation complexes
   - Réutilisabilité

5. **Helper Pattern**
   - Centralisation des opérations de recherche
   - Gestion uniforme des erreurs

### Principes SOLID

1. **Single Responsibility Principle (SRP)**

   - Chaque classe a une seule raison de changer
   - Séparation Service / Validator / Helper

2. **Open/Closed Principle (OCP)**

   - Extensible via interfaces
   - Fermé aux modifications directes

3. **Liskov Substitution Principle (LSP)**

   - Les implémentations respectent les contrats d'interface

4. **Interface Segregation Principle (ISP)**

   - Interfaces spécifiques par service

5. **Dependency Inversion Principle (DIP)**
   - Dépendances vers les abstractions (interfaces)
   - Injection de dépendances

## Aspects transversaux (AOP)

### Logging

**Aspect :** `LoggingAspect`

**Fonctionnalités :**

- Logging automatique des appels de méthodes
- Mesure du temps d'exécution
- Logging des paramètres et résultats

**Annotations :**

- `@LogOperation(description = "...")` : Log une opération métier
- `@LogExecutionTime` : Mesure et log le temps d'exécution

**Implémentation :**

```java
@Around("@annotation(logOperation)")
public Object logOperation(ProceedingJoinPoint joinPoint, LogOperation logOperation) {
    // Log avant
    Object result = joinPoint.proceed();
    // Log après avec résultat
    return result;
}
```

### Gestion des exceptions

**Composant :** `GlobalExceptionHandler`

**Fonctionnalités :**

- Capture centralisée des exceptions
- Transformation en réponses HTTP cohérentes
- Logging des erreurs

**Exceptions gérées :**

- `ResourceNotFoundException` → 404 Not Found
- `BusinessException` → 400 Bad Request
- `DuplicateResourceException` → 409 Conflict
- `MethodArgumentNotValidException` → 400 Bad Request (validation)
- `Exception` → 500 Internal Server Error

**Format de réponse d'erreur :**

```json
{
  "timestamp": "2025-12-07T10:30:00",
  "status": 404,
  "error": "Not Found",
  "message": "User Story not found with id: abc123",
  "path": "/api/user-stories/abc123"
}
```

## Validation

### Niveaux de validation

1. **Validation au niveau DTO (@Valid)**

   - Annotations Bean Validation (JSR-380)
   - `@NotBlank`, `@Size`, `@NotNull`, etc.
   - Validation automatique par Spring

2. **Validation personnalisée**

   - `@StartEndDateValidator` : Vérifie que endDate > startDate
   - `@EnumValid` : Valide les valeurs d'énumération

3. **Validation métier (Validators)**
   - Règles métier complexes
   - Validation inter-entités
   - Exemple : `UserStoryValidator.validateCanMarkAsDone()`

### Exemple de validation en cascade

```
Request DTO (@Valid)
    ↓
Controller validation
    ↓
Service layer
    ↓
Business Validator
    ↓
Entity constraints
```

## Gestion des transactions

**Stratégie :** Déclarative avec `@Transactional`

**Niveau :** Couche Service

**Configuration :**

- Propagation : `REQUIRED` (par défaut)
- Isolation : `DEFAULT`
- Rollback : Sur toutes les `RuntimeException`

**Exemples :**

```java
@Transactional
public UserStoryResponse create(UserStoryRequest dto) {
    // Toute exception déclenche un rollback
}

@Transactional(readOnly = true)
public UserStoryResponse getUserStoryById(UUID id) {
    // Optimisation pour lecture seule
}
```

## Sécurité

### Actuellement implémenté

- Validation des entrées
- Gestion des erreurs sans exposition de détails sensibles
- Protection contre les injections SQL (JPA)

### À implémenter (recommandations)

- Spring Security pour l'authentification/autorisation
- JWT pour les tokens
- HTTPS en production
- Rate limiting
- CORS configuré selon les besoins

## Performance

### Optimisations mises en place

1. **Lazy Loading**

   - Relations `@ManyToOne(fetch = FetchType.LAZY)`
   - Chargement à la demande

2. **Cascade operations**

   - `cascade = CascadeType.ALL` sur relations parent-enfant
   - Réduction du nombre de requêtes

3. **@ToString.Exclude / @EqualsAndHashCode.Exclude**

   - Évite les chargements non nécessaires
   - Prévient les références circulaires

4. **Transactions read-only**
   - Optimisation pour les opérations de lecture

### Recommandations futures

- Mise en cache (Spring Cache, Redis)
- Pagination systématique des listes
- Projections JPA pour requêtes spécifiques
- Connection pooling (HikariCP déjà inclus avec Spring Boot)

## Monitoring et Observabilité

### Spring Actuator

**Endpoints disponibles :**

- `/actuator/health` : État de santé de l'application
- `/actuator/info` : Informations sur l'application
- `/actuator/metrics` : Métriques applicatives

### Logging

**Niveaux :**

- `DEBUG` : Package `ma.ensa.apms`
- `INFO` : Spring Framework
- `ERROR` : Erreurs applicatives

**Format :**

```
[timestamp] LEVEL [thread] logger - message
```

## Déploiement

### Options de déploiement

1. **Traditionnel (JAR)**

   ```bash
   mvn clean package
   java -jar target/APMS-0.0.1-SNAPSHOT.jar
   ```

2. **Docker**

   ```bash
   docker build -t apms:latest .
   docker run -p 8080:8080 apms:latest
   ```

3. **Docker Compose**
   ```bash
   docker-compose up
   ```

### Variables d'environnement recommandées

```bash
SPRING_DATASOURCE_URL=jdbc:postgresql://db:5432/apms_db
SPRING_DATASOURCE_USERNAME=apms_user
SPRING_DATASOURCE_PASSWORD=secure_password
SPRING_JPA_HIBERNATE_DDL_AUTO=validate  # En production
SERVER_PORT=8080
```

## Évolutions futures

### Fonctionnalités

- [ ] Authentification et autorisation
- [ ] Gestion des utilisateurs et rôles
- [ ] Notifications (email, push)
- [ ] Dashboard analytique
- [ ] Export de rapports
- [ ] Intégration CI/CD
- [ ] WebSockets pour mises à jour temps réel

### Techniques

- [ ] Migration vers architecture hexagonale
- [ ] Event-driven architecture (Kafka, RabbitMQ)
- [ ] CQRS pour séparation lecture/écriture
- [ ] GraphQL en complément de REST
- [ ] Microservices si nécessaire

## Diagrammes

### Diagramme de flux - Création d'une User Story

```
Client → Controller → Service → Validator
                         ↓
                      Mapper → Entity
                         ↓
                    Repository → Database
                         ↓
                      Entity → Mapper → DTO
                         ↓
                    Controller → Client
```

### Diagramme de séquence - Changement de statut

```
UserStoryController → UserStoryService
                          ↓
                   RepositoryHelper.findById()
                          ↓
                   Validator.validateCanMarkAsDone()
                          ↓
                   Entity.setStatus(DONE)
                          ↓
                   Repository.save()
                          ↓
                   Mapper.toResponse()
                          ↓
                   Return UserStoryResponse
```

---

**Document d'architecture - Version 1.0**
_Dernière mise à jour : Décembre 2025_
