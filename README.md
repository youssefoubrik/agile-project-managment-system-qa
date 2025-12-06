# Agile Project Management System (APMS)

![Java](https://img.shields.io/badge/Java-17-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.3-brightgreen)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-Database-blue)
![Maven](https://img.shields.io/badge/Maven-Build-red)

## 📋 Table des matières

- [Description](#description)
- [Architecture](#architecture)
- [Technologies utilisées](#technologies-utilisées)
- [Prérequis](#prérequis)
- [Installation](#installation)
- [Configuration](#configuration)
- [Lancement de l'application](#lancement-de-lapplication)
- [API REST](#api-rest)
- [Tests](#tests)
- [Structure du projet](#structure-du-projet)
- [Modèle de données](#modèle-de-données)
- [Fonctionnalités](#fonctionnalités)
- [Contribuer](#contribuer)

## 📖 Description

**APMS (Agile Project Management System)** est une application de gestion de projets Agile/Scrum complète développée avec Spring Boot. Le système permet de gérer l'ensemble du cycle de vie d'un projet Agile, incluant :

- Gestion des projets et Product Backlogs
- Création et organisation d'Epics et User Stories
- Planification des Sprints
- Définition des critères d'acceptation (format BDD)
- Décomposition en tâches techniques
- Suivi des statuts et progression

## 🏗️ Architecture

L'application suit une architecture en couches respectant les principes SOLID et les bonnes pratiques Spring Boot :

```
┌─────────────────────────────────────────┐
│          Controllers (REST API)         │
│     ProjectController, EpicController,  │
│     UserStoryController, TaskController │
└────────────────┬────────────────────────┘
                 │
┌────────────────▼────────────────────────┐
│            Services (Business)          │
│      ProjectService, UserStoryService   │
│      Validators, Helpers                │
└────────────────┬────────────────────────┘
                 │
┌────────────────▼────────────────────────┐
│         Repositories (Data)             │
│      JPA Repositories, Entities         │
└────────────────┬────────────────────────┘
                 │
┌────────────────▼────────────────────────┐
│         PostgreSQL Database             │
└─────────────────────────────────────────┘
```

### Couches principales

1. **Controllers** : Points d'entrée REST API avec validation des requêtes
2. **Services** : Logique métier et orchestration
3. **Validators** : Validation des règles métier complexes
4. **Helpers** : Méthodes utilitaires de recherche avec gestion d'erreurs
5. **Mappers** : Transformation entre entités et DTOs (MapStruct)
6. **Repositories** : Accès aux données via Spring Data JPA
7. **Entities** : Modèles de domaine avec relations JPA

## 🛠️ Technologies utilisées

### Backend

- **Java 17** - Langage de programmation
- **Spring Boot 3.4.3** - Framework principal
  - Spring Data JPA - Couche de persistance
  - Spring Web - API REST
  - Spring AOP - Aspects (logging)
  - Spring Validation - Validation des données
  - Spring Actuator - Monitoring
- **Hibernate 6.6.8** - ORM
- **MapStruct 1.6.3** - Mapping objet-objet
- **Lombok** - Réduction du code boilerplate

### Base de données

- **PostgreSQL** - Base de données relationnelle

### Build & Tests

- **Maven** - Gestion des dépendances
- **JUnit 5** - Tests unitaires
- **Mockito** - Mocking pour les tests
- **JaCoCo** - Couverture de code
- **SonarQube** - Analyse de qualité du code

### Autres

- **Docker** - Conteneurisation (docker-compose.yml)
- **SLF4J/Logback** - Logging

## ✅ Prérequis

- **Java 17** ou supérieur
- **Maven 3.6+**
- **PostgreSQL 12+** (ou Docker pour lancer via docker-compose)
- **Git**

## 📦 Installation

### 1. Cloner le repository

```bash
git clone https://github.com/youssefoubrik/agile-project-managment-system-qa.git
cd agile-project-management-system-qa
```

### 2. Configuration de la base de données

#### Option A : Utiliser Docker (recommandé)

```bash
docker-compose up -d
```

Cela lancera une instance PostgreSQL configurée automatiquement.

#### Option B : Installation manuelle PostgreSQL

1. Installez PostgreSQL
2. Créez une base de données :

```sql
CREATE DATABASE apms_db;
CREATE USER apms_user WITH PASSWORD 'your_password';
GRANT ALL PRIVILEGES ON DATABASE apms_db TO apms_user;
```

### 3. Configuration de l'application

Créez ou modifiez le fichier `src/main/resources/application.properties` :

```properties
# Database Configuration
spring.datasource.url=jdbc:postgresql://localhost:5432/apms_db
spring.datasource.username=apms_user
spring.datasource.password=your_password

# JPA/Hibernate
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
spring.jpa.properties.hibernate.format_sql=true

# Server
server.port=8080

# Logging
logging.level.ma.ensa.apms=DEBUG
logging.level.org.springframework.web=INFO
```

## 🚀 Lancement de l'application

### Avec Maven Wrapper (recommandé)

```bash
# Linux/Mac
./mvnw clean install
./mvnw spring-boot:run

# Windows
mvnw.cmd clean install
mvnw.cmd spring-boot:run
```

### Avec Maven installé

```bash
mvn clean install
mvn spring-boot:run
```

L'application sera accessible à l'adresse : **http://localhost:8080**

## 🔌 API REST

### Endpoints principaux

#### Projects

```http
POST   /api/projects              # Créer un projet
GET    /api/projects/{id}         # Récupérer un projet
PUT    /api/projects/{id}         # Mettre à jour un projet
DELETE /api/projects/{id}         # Supprimer un projet
```

#### Epics

```http
POST   /api/epics                 # Créer un Epic
GET    /api/epics/{id}            # Récupérer un Epic
PUT    /api/epics/{id}            # Mettre à jour un Epic
DELETE /api/epics/{id}            # Supprimer un Epic
GET    /api/epics/product-backlog/{id}  # Epics par Product Backlog
```

#### User Stories

```http
POST   /api/user-stories          # Créer une User Story
GET    /api/user-stories/{id}     # Récupérer une User Story
PUT    /api/user-stories/{id}     # Mettre à jour une User Story
DELETE /api/user-stories/{id}     # Supprimer une User Story
PATCH  /api/user-stories/{id}/status  # Changer le statut
POST   /api/user-stories/{storyId}/link-epic/{epicId}  # Lier à un Epic
POST   /api/user-stories/{storyId}/move-sprint/{sprintId}  # Déplacer vers Sprint
```

#### Tasks

```http
POST   /api/tasks                 # Créer une tâche
GET    /api/tasks/{id}            # Récupérer une tâche
PUT    /api/tasks/{id}            # Mettre à jour une tâche
DELETE /api/tasks/{id}            # Supprimer une tâche
PATCH  /api/tasks/{id}/status     # Changer le statut
```

#### Acceptance Criteria

```http
POST   /api/acceptance-criteria   # Créer un critère
GET    /api/acceptance-criteria/{id}  # Récupérer un critère
PUT    /api/acceptance-criteria/{id}  # Mettre à jour un critère
DELETE /api/acceptance-criteria/{id}  # Supprimer un critère
PATCH  /api/acceptance-criteria/{id}/met  # Marquer comme satisfait
```

### Exemples de requêtes

#### Créer un projet

```bash
curl -X POST http://localhost:8080/api/projects \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Mon Projet Agile",
    "description": "Description du projet",
    "status": "ACTIVE",
    "startDate": "2025-01-01T09:00:00",
    "endDate": "2025-12-31T18:00:00"
  }'
```

#### Créer une User Story

```bash
curl -X POST http://localhost:8080/api/user-stories \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Connexion utilisateur",
    "role": "utilisateur",
    "feature": "me connecter avec email et mot de passe",
    "benefit": "accéder à mon espace personnel",
    "priority": 1,
    "productBacklogId": "uuid-du-product-backlog"
  }'
```

## 🧪 Tests

### Exécuter tous les tests

```bash
./mvnw test
```

### Exécuter les tests avec couverture de code (JaCoCo)

```bash
./mvnw clean test jacoco:report
```

Le rapport de couverture sera disponible dans : `target/jacoco/index.html`

### Analyse SonarQube

```bash
./mvnw clean verify sonar:sonar \
  -Dsonar.projectKey=apms \
  -Dsonar.host.url=http://localhost:9000 \
  -Dsonar.login=your-token
```

## 📁 Structure du projet

```
src/
├── main/
│   ├── java/ma/ensa/apms/
│   │   ├── advice/              # Gestion globale des exceptions
│   │   ├── annotation/          # Annotations personnalisées
│   │   ├── aspect/              # Aspects AOP (logging)
│   │   ├── config/              # Configuration Spring
│   │   ├── controller/          # Contrôleurs REST
│   │   ├── dto/                 # Data Transfer Objects
│   │   │   ├── Request/         # DTOs de requête
│   │   │   └── Response/        # DTOs de réponse
│   │   ├── exception/           # Exceptions personnalisées
│   │   ├── logging/             # Utilitaires de logging
│   │   ├── mapper/              # Mappers MapStruct
│   │   ├── modal/               # Entités JPA
│   │   │   └── enums/           # Énumérations
│   │   ├── repository/          # Repositories Spring Data
│   │   ├── service/             # Interfaces de services
│   │   │   ├── impl/            # Implémentations
│   │   │   ├── helper/          # Helpers métier
│   │   │   └── validator/       # Validateurs métier
│   │   └── validation/          # Validateurs de contraintes
│   └── resources/
│       └── application.properties
└── test/                        # Tests unitaires et d'intégration
```

## 💾 Modèle de données

### Hiérarchie des entités

```
Project (Projet)
    └── ProductBacklog
        ├── Epic
        │   └── UserStory
        │       ├── AcceptanceCriteria
        │       └── Task
        └── SprintBacklog
            └── UserStory
```

### Entités principales

- **Project** : Projet principal avec dates de début/fin et statut
- **ProductBacklog** : Backlog produit contenant tous les éléments
- **Epic** : Grande fonctionnalité regroupant des User Stories
- **UserStory** : Exigence utilisateur au format "En tant que... je veux... afin de..."
- **SprintBacklog** : Backlog de sprint pour la planification
- **AcceptanceCriteria** : Critères d'acceptation au format BDD (Given-When-Then)
- **Task** : Tâche technique décomposée depuis une User Story

### Énumérations

- **ProjectStatus** : ACTIVE, COMPLETED, ON_HOLD, CANCELLED
- **UserStoryStatus** : TODO, IN_PROGRESS, IN_REVIEW, DONE, BLOCKED
- **TaskStatus** : TODO, IN_PROGRESS, DONE, BLOCKED

## ⚡ Fonctionnalités

### ✅ Gestion de projet

- Création et gestion de projets avec dates et statuts
- Création automatique du Product Backlog

### ✅ Product Backlog

- Organisation hiérarchique des exigences
- Priorisation des User Stories
- Regroupement en Epics

### ✅ User Stories

- Format standard "As a... I want... So that..."
- Liaison aux Epics pour organisation thématique
- Assignation aux Sprints
- Gestion du cycle de vie (statuts)
- Validation avant passage en "Done"

### ✅ Critères d'acceptation

- Format BDD (Given-When-Then)
- Marquage de satisfaction
- Validation obligatoire pour clore une User Story

### ✅ Gestion des tâches

- Décomposition des User Stories en tâches techniques
- Suivi avec dates et statuts
- Validation des dates (fin > début)

### ✅ Sprint Planning

- Création de Sprint Backlogs
- Assignation de User Stories aux Sprints
- Suivi de la progression

### ✅ Aspects techniques

- Validation automatique des données
- Gestion d'erreurs centralisée
- Logging des opérations (AOP)
- Audit automatique (createdAt, updatedAt)
- Transactions gérées
- Mappage automatique entités ↔ DTOs

## 🤝 Contribuer

Les contributions sont les bienvenues ! Voici comment contribuer :

1. Fork le projet
2. Créez une branche pour votre fonctionnalité (`git checkout -b feature/AmazingFeature`)
3. Committez vos changements (`git commit -m 'Add some AmazingFeature'`)
4. Poussez vers la branche (`git push origin feature/AmazingFeature`)
5. Ouvrez une Pull Request

### Standards de code

- Respecter les conventions Java et Spring Boot
- Ajouter des tests pour les nouvelles fonctionnalités
- Documenter avec Javadoc
- Maintenir une couverture de code > 80%
- Suivre les principes SOLID

## 📄 Licence

Ce projet est développé dans un cadre éducatif.

## 👥 Auteurs

- **APMS Team** - _Développement initial_

## 📞 Contact

Pour toute question ou suggestion, n'hésitez pas à ouvrir une issue sur GitHub.

---

**Fait avec ❤️ pour la communauté Agile**
