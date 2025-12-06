# Guide API - APMS

Documentation complète de l'API REST du système APMS.

## 📋 Table des matières

- [Informations générales](#informations-générales)
- [Authentification](#authentification)
- [Format des réponses](#format-des-réponses)
- [Gestion des erreurs](#gestion-des-erreurs)
- [Endpoints](#endpoints)
  - [Projects](#projects)
  - [Product Backlogs](#product-backlogs)
  - [Epics](#epics)
  - [User Stories](#user-stories)
  - [Sprint Backlogs](#sprint-backlogs)
  - [Tasks](#tasks)
  - [Acceptance Criteria](#acceptance-criteria)

## ℹ️ Informations générales

**Base URL :** `http://localhost:8080`

**Format de données :** JSON

**Headers requis :**

```http
Content-Type: application/json
Accept: application/json
```

## 🔐 Authentification

> ⚠️ **Note :** L'authentification n'est pas encore implémentée dans cette version.
> À venir : JWT Bearer Token

## 📦 Format des réponses

### Succès

```json
{
  "id": "uuid",
  "field1": "value1",
  "field2": "value2",
  "createdAt": "2025-12-07T10:30:00",
  "updatedAt": "2025-12-07T10:30:00"
}
```

### Liste

```json
[
  {
    "id": "uuid1",
    "name": "Item 1"
  },
  {
    "id": "uuid2",
    "name": "Item 2"
  }
]
```

## ❌ Gestion des erreurs

### Format d'erreur

```json
{
  "timestamp": "2025-12-07T10:30:00",
  "status": 404,
  "error": "Not Found",
  "message": "User Story not found with id: abc123",
  "path": "/api/user-stories/abc123"
}
```

### Codes HTTP

| Code | Signification         | Description           |
| ---- | --------------------- | --------------------- |
| 200  | OK                    | Succès                |
| 201  | Created               | Ressource créée       |
| 204  | No Content            | Suppression réussie   |
| 400  | Bad Request           | Données invalides     |
| 404  | Not Found             | Ressource introuvable |
| 409  | Conflict              | Ressource en conflit  |
| 500  | Internal Server Error | Erreur serveur        |

---

## 📌 Endpoints

## Projects

### Créer un projet

```http
POST /api/projects
```

**Body :**

```json
{
  "name": "Mon Projet Agile",
  "description": "Description du projet",
  "status": "ACTIVE",
  "startDate": "2025-01-01T09:00:00",
  "endDate": "2025-12-31T18:00:00"
}
```

**Réponse (201 Created) :**

```json
{
  "id": "550e8400-e29b-41d4-a716-446655440000",
  "name": "Mon Projet Agile",
  "description": "Description du projet",
  "status": "ACTIVE",
  "startDate": "2025-01-01T09:00:00",
  "endDate": "2025-12-31T18:00:00",
  "productBacklogId": "660e8400-e29b-41d4-a716-446655440001",
  "createdAt": "2025-12-07T10:30:00",
  "updatedAt": "2025-12-07T10:30:00"
}
```

**Validations :**

- `name` : Obligatoire, non vide
- `status` : Valeurs autorisées : `ACTIVE`, `COMPLETED`, `ON_HOLD`, `CANCELLED`
- `endDate` : Doit être postérieure à `startDate`

---

### Récupérer un projet

```http
GET /api/projects/{id}
```

**Réponse (200 OK) :**

```json
{
  "id": "550e8400-e29b-41d4-a716-446655440000",
  "name": "Mon Projet Agile",
  "description": "Description du projet",
  "status": "ACTIVE",
  "startDate": "2025-01-01T09:00:00",
  "endDate": "2025-12-31T18:00:00",
  "productBacklogId": "660e8400-e29b-41d4-a716-446655440001",
  "createdAt": "2025-12-07T10:30:00",
  "updatedAt": "2025-12-07T10:30:00"
}
```

**Erreurs :**

- `404` : Projet non trouvé

---

### Mettre à jour un projet

```http
PUT /api/projects/{id}
```

**Body :**

```json
{
  "name": "Nouveau nom",
  "description": "Nouvelle description",
  "status": "ACTIVE",
  "startDate": "2025-01-01T09:00:00",
  "endDate": "2025-12-31T18:00:00"
}
```

**Réponse (200 OK) :**

```json
{
  "id": "550e8400-e29b-41d4-a716-446655440000",
  "name": "Nouveau nom",
  "description": "Nouvelle description",
  "status": "ACTIVE",
  "startDate": "2025-01-01T09:00:00",
  "endDate": "2025-12-31T18:00:00",
  "productBacklogId": "660e8400-e29b-41d4-a716-446655440001",
  "createdAt": "2025-12-07T10:30:00",
  "updatedAt": "2025-12-07T11:30:00"
}
```

---

### Supprimer un projet

```http
DELETE /api/projects/{id}
```

**Réponse (204 No Content)**

⚠️ **Attention :** Supprime également le Product Backlog et tous les éléments associés en cascade.

---

## Epics

### Créer un Epic

```http
POST /api/epics
```

**Body :**

```json
{
  "name": "Gestion des utilisateurs",
  "description": "Epic regroupant toutes les fonctionnalités liées aux utilisateurs",
  "productBacklogId": "660e8400-e29b-41d4-a716-446655440001"
}
```

**Réponse (201 Created) :**

```json
{
  "id": "770e8400-e29b-41d4-a716-446655440002",
  "name": "Gestion des utilisateurs",
  "description": "Epic regroupant toutes les fonctionnalités liées aux utilisateurs",
  "productBacklogId": "660e8400-e29b-41d4-a716-446655440001",
  "createdAt": "2025-12-07T10:30:00",
  "updatedAt": "2025-12-07T10:30:00"
}
```

**Validations :**

- `name` : 3-100 caractères, obligatoire
- `description` : Maximum 1000 caractères
- `productBacklogId` : Doit exister

---

### Récupérer les Epics d'un Product Backlog

```http
GET /api/epics/product-backlog/{productBacklogId}
```

**Réponse (200 OK) :**

```json
[
  {
    "id": "770e8400-e29b-41d4-a716-446655440002",
    "name": "Gestion des utilisateurs",
    "description": "Description de l'Epic",
    "productBacklogId": "660e8400-e29b-41d4-a716-446655440001"
  },
  {
    "id": "770e8400-e29b-41d4-a716-446655440003",
    "name": "Gestion des projets",
    "description": "Description de l'Epic",
    "productBacklogId": "660e8400-e29b-41d4-a716-446655440001"
  }
]
```

---

### Récupérer un Epic

```http
GET /api/epics/{id}
```

---

### Mettre à jour un Epic

```http
PUT /api/epics/{id}
```

---

### Supprimer un Epic

```http
DELETE /api/epics/{id}
```

⚠️ **Attention :** Supprime également toutes les User Stories liées en cascade.

---

## User Stories

### Créer une User Story

```http
POST /api/user-stories
```

**Body :**

```json
{
  "name": "Connexion utilisateur",
  "role": "utilisateur",
  "feature": "me connecter avec email et mot de passe",
  "benefit": "accéder à mon espace personnel",
  "priority": 1,
  "productBacklogId": "660e8400-e29b-41d4-a716-446655440001"
}
```

**Réponse (201 Created) :**

```json
{
  "id": "880e8400-e29b-41d4-a716-446655440004",
  "name": "Connexion utilisateur",
  "role": "utilisateur",
  "feature": "me connecter avec email et mot de passe",
  "benefit": "accéder à mon espace personnel",
  "priority": 1,
  "status": "TODO",
  "productBacklogId": "660e8400-e29b-41d4-a716-446655440001",
  "epicId": null,
  "sprintBacklogId": null
}
```

**Validations :**

- `name` : 3-100 caractères, obligatoire
- `role`, `feature`, `benefit` : Obligatoires, non vides
- `priority` : Obligatoire
- `productBacklogId` : Doit exister

---

### Récupérer une User Story

```http
GET /api/user-stories/{id}
```

---

### Mettre à jour une User Story

```http
PUT /api/user-stories/{id}
```

**Body :**

```json
{
  "name": "Nouveau nom",
  "role": "utilisateur",
  "feature": "nouvelle feature",
  "benefit": "nouveau bénéfice",
  "priority": 2
}
```

---

### Changer le statut d'une User Story

```http
PATCH /api/user-stories/{id}/status
```

**Body :**

```json
{
  "status": "IN_PROGRESS"
}
```

**Statuts disponibles :**

- `TODO`
- `IN_PROGRESS`
- `IN_REVIEW`
- `DONE`
- `BLOCKED`

**Règles métier :**

- Pour passer en `DONE`, tous les critères d'acceptation doivent être satisfaits

---

### Lier une User Story à un Epic

```http
POST /api/user-stories/{storyId}/link-epic/{epicId}
```

**Réponse (200 OK) :**

```json
{
  "id": "880e8400-e29b-41d4-a716-446655440004",
  "name": "Connexion utilisateur",
  "epicId": "770e8400-e29b-41d4-a716-446655440002",
  "status": "TODO",
  ...
}
```

**Règles métier :**

- La User Story doit être en statut `TODO`

---

### Déplacer une User Story vers un Sprint

```http
POST /api/user-stories/{storyId}/move-sprint/{sprintId}
```

**Réponse (200 OK) :**

```json
{
  "id": "880e8400-e29b-41d4-a716-446655440004",
  "name": "Connexion utilisateur",
  "sprintBacklogId": "990e8400-e29b-41d4-a716-446655440005",
  "productBacklogId": null,
  ...
}
```

---

### Récupérer les critères d'acceptation d'une User Story

```http
GET /api/user-stories/{id}/acceptance-criteria
```

**Réponse (200 OK) :**

```json
[
  {
    "id": "aa0e8400-e29b-41d4-a716-446655440006",
    "given": "l'utilisateur est sur la page de connexion",
    "when": "il entre un email et mot de passe valides",
    "then": "il est redirigé vers son tableau de bord",
    "met": true
  }
]
```

---

### Récupérer les User Stories par statut et Product Backlog

```http
GET /api/user-stories/status/{status}/product-backlog/{productBacklogId}
```

---

### Récupérer les User Stories d'un Epic

```http
GET /api/user-stories/epic/{epicId}
```

---

### Récupérer les User Stories d'un Sprint

```http
GET /api/user-stories/sprint/{sprintId}
```

---

### Supprimer une User Story

```http
DELETE /api/user-stories/{id}
```

⚠️ **Attention :** Supprime également tous les critères d'acceptation et tâches en cascade.

---

## Sprint Backlogs

### Créer un Sprint Backlog

```http
POST /api/sprint-backlogs
```

**Body :**

```json
{
  "name": "Sprint 1",
  "productBacklogId": "660e8400-e29b-41d4-a716-446655440001"
}
```

**Réponse (201 Created) :**

```json
{
  "id": "990e8400-e29b-41d4-a716-446655440005",
  "name": "Sprint 1",
  "productBacklogId": "660e8400-e29b-41d4-a716-446655440001",
  "createdAt": "2025-12-07T10:30:00",
  "updatedAt": "2025-12-07T10:30:00"
}
```

**Validations :**

- `name` : 3-50 caractères, obligatoire

---

### Récupérer un Sprint Backlog

```http
GET /api/sprint-backlogs/{id}
```

---

### Récupérer les Sprint Backlogs d'un Product Backlog

```http
GET /api/sprint-backlogs/product-backlog/{productBacklogId}
```

---

### Mettre à jour un Sprint Backlog

```http
PUT /api/sprint-backlogs/{id}
```

---

### Supprimer un Sprint Backlog

```http
DELETE /api/sprint-backlogs/{id}
```

---

## Tasks

### Créer une tâche

```http
POST /api/tasks
```

**Body :**

```json
{
  "title": "Créer le formulaire de connexion",
  "description": "Créer un formulaire HTML/CSS pour la page de connexion",
  "status": "TODO",
  "startDate": "2025-12-08T09:00:00",
  "endDate": "2025-12-10T18:00:00",
  "userStoryId": "880e8400-e29b-41d4-a716-446655440004"
}
```

**Réponse (201 Created) :**

```json
{
  "id": "bb0e8400-e29b-41d4-a716-446655440007",
  "title": "Créer le formulaire de connexion",
  "description": "Créer un formulaire HTML/CSS pour la page de connexion",
  "status": "TODO",
  "startDate": "2025-12-08T09:00:00",
  "endDate": "2025-12-10T18:00:00",
  "userStoryId": "880e8400-e29b-41d4-a716-446655440004"
}
```

**Validations :**

- `title` : 3-100 caractères, obligatoire
- `description` : Obligatoire
- `status` : Valeurs : `TODO`, `IN_PROGRESS`, `DONE`, `BLOCKED`
- `endDate` : Doit être postérieure à `startDate`

---

### Récupérer une tâche

```http
GET /api/tasks/{id}
```

---

### Mettre à jour une tâche

```http
PUT /api/tasks/{id}
```

---

### Changer le statut d'une tâche

```http
PATCH /api/tasks/{id}/status
```

**Body :**

```json
{
  "status": "IN_PROGRESS"
}
```

---

### Mettre à jour la date de début

```http
PATCH /api/tasks/{id}/start-date
```

**Body :**

```json
{
  "startDate": "2025-12-08T09:00:00"
}
```

---

### Mettre à jour la date de fin

```http
PATCH /api/tasks/{id}/end-date
```

**Body :**

```json
{
  "endDate": "2025-12-10T18:00:00"
}
```

---

### Récupérer les tâches d'une User Story

```http
GET /api/tasks/user-story/{userStoryId}
```

---

### Supprimer une tâche

```http
DELETE /api/tasks/{id}
```

---

## Acceptance Criteria

### Créer un critère d'acceptation

```http
POST /api/acceptance-criteria
```

**Body :**

```json
{
  "given": "l'utilisateur est sur la page de connexion",
  "when": "il entre un email et mot de passe valides",
  "then": "il est redirigé vers son tableau de bord",
  "met": false,
  "userStoryId": "880e8400-e29b-41d4-a716-446655440004"
}
```

**Réponse (201 Created) :**

```json
{
  "id": "aa0e8400-e29b-41d4-a716-446655440006",
  "given": "l'utilisateur est sur la page de connexion",
  "when": "il entre un email et mot de passe valides",
  "then": "il est redirigé vers son tableau de bord",
  "met": false,
  "userStoryId": "880e8400-e29b-41d4-a716-446655440004"
}
```

**Validations :**

- `given`, `when`, `then` : 5-255 caractères, obligatoires
- `met` : Obligatoire (boolean)

---

### Récupérer un critère d'acceptation

```http
GET /api/acceptance-criteria/{id}
```

---

### Mettre à jour un critère d'acceptation

```http
PUT /api/acceptance-criteria/{id}
```

---

### Marquer un critère comme satisfait

```http
PATCH /api/acceptance-criteria/{id}/met
```

**Body :**

```json
{
  "met": true
}
```

---

### Supprimer un critère d'acceptation

```http
DELETE /api/acceptance-criteria/{id}
```

---

## 📊 Exemples de flux complets

### Créer un projet complet

1. **Créer le projet**

   ```bash
   POST /api/projects
   ```

2. **Le Product Backlog est créé automatiquement**

3. **Créer des Epics**

   ```bash
   POST /api/epics
   ```

4. **Créer des User Stories**

   ```bash
   POST /api/user-stories
   ```

5. **Lier les User Stories aux Epics**

   ```bash
   POST /api/user-stories/{storyId}/link-epic/{epicId}
   ```

6. **Ajouter des critères d'acceptation**

   ```bash
   POST /api/acceptance-criteria
   ```

7. **Créer un Sprint Backlog**

   ```bash
   POST /api/sprint-backlogs
   ```

8. **Déplacer des User Stories vers le Sprint**

   ```bash
   POST /api/user-stories/{storyId}/move-sprint/{sprintId}
   ```

9. **Créer des tâches**

   ```bash
   POST /api/tasks
   ```

10. **Suivre l'avancement**
    ```bash
    PATCH /api/tasks/{id}/status
    PATCH /api/user-stories/{id}/status
    ```

---

## 🔍 Tips et bonnes pratiques

### Pagination

> ⚠️ **À venir :** La pagination n'est pas encore implémentée. Les listes retournent tous les éléments.

### Filtrage et tri

Utilisez les endpoints spécifiques pour filtrer :

- Par statut : `/api/user-stories/status/{status}/...`
- Par Epic : `/api/user-stories/epic/{epicId}`
- Par Sprint : `/api/user-stories/sprint/{sprintId}`

### Performance

- Utilisez les endpoints spécifiques plutôt que récupérer tout puis filtrer côté client
- Les relations sont chargées en lazy par défaut

### Gestion des erreurs

- Vérifiez toujours le code de statut HTTP
- Le champ `message` contient un message lisible par l'utilisateur
- Le champ `path` indique l'endpoint appelé

---

**Documentation API - Version 1.0**
_Dernière mise à jour : Décembre 2025_
