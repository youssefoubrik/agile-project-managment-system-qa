# Glossaire et Terminologie - APMS

Ce document définit tous les termes et concepts utilisés dans le projet APMS.

## 📚 Méthodologie Agile/Scrum

### Product Owner

Responsable de maximiser la valeur du produit. Gère le Product Backlog et définit les priorités.

### Scrum Master

Facilite le processus Scrum et aide l'équipe à suivre les pratiques Agile.

### Sprint

Itération de développement de durée fixe (généralement 1-4 semaines) où l'équipe développe un incrément de produit potentiellement livrable.

### Incrément

Résultat d'un Sprint : l'ensemble des éléments du Product Backlog complétés durant le Sprint.

### Daily Scrum

Réunion quotidienne de 15 minutes pour synchroniser l'équipe.

### Sprint Planning

Réunion où l'équipe planifie le travail du prochain Sprint.

### Sprint Review

Réunion de fin de Sprint pour inspecter l'incrément et adapter le Product Backlog.

### Sprint Retrospective

Réunion de fin de Sprint pour que l'équipe réfléchisse sur son processus et identifie des améliorations.

### Velocity

Mesure de la quantité de travail qu'une équipe peut accomplir durant un Sprint.

### Burndown Chart

Graphique montrant le travail restant dans le temps.

## 📦 Entités du domaine

### Project (Projet)

**Définition :** Conteneur principal représentant un projet complet avec des objectifs, des dates et un statut.

**Attributs clés :**

- Nom et description
- Dates de début et fin
- Statut (ACTIVE, COMPLETED, ON_HOLD, CANCELLED)
- Un Product Backlog unique

**Relations :**

- 1-1 avec Product Backlog

### Product Backlog

**Définition :** Liste ordonnée et priorisée de tout ce qui doit être développé pour le produit. Source unique de vérité pour les exigences.

**Contenu :**

- Epics
- User Stories
- Sprint Backlogs

**Responsable :** Product Owner

**Caractéristiques :**

- Vivant et évolutif
- Ordonné par priorité
- Estimé (story points, heures, etc.)

### Epic

**Définition :** Grande fonctionnalité ou initiative métier qui regroupe plusieurs User Stories liées. Trop volumineuse pour un seul Sprint.

**Exemples :**

- "Système d'authentification"
- "Gestion des paiements"
- "Module de reporting"

**Décomposition :**

- Un Epic contient plusieurs User Stories
- Les User Stories sont des implémentations concrètes de l'Epic

**Quand créer un Epic :**

- Fonctionnalité nécessitant plusieurs Sprints
- Regroupement thématique souhaité
- Initiative stratégique importante

### User Story

**Définition :** Description simple d'une fonctionnalité du point de vue de l'utilisateur final.

**Format standard :**

```
En tant que [rôle]
Je veux [fonctionnalité/action]
Afin de [bénéfice/valeur]
```

**Exemple concret :**

```
En tant qu'utilisateur
Je veux pouvoir réinitialiser mon mot de passe par email
Afin de récupérer l'accès à mon compte si je l'oublie
```

**Critères INVEST :**

- **I**ndependent : Indépendante des autres
- **N**egotiable : Négociable, pas un contrat fixe
- **V**aluable : Apporte de la valeur à l'utilisateur
- **E**stimable : Peut être estimée
- **S**mall : Petite, réalisable en un Sprint
- **T**estable : Testable avec des critères clairs

**Cycle de vie :**

1. TODO : Nouvellement créée, non commencée
2. IN_PROGRESS : En cours de développement
3. IN_REVIEW : En revue/test
4. DONE : Complétée, tous critères satisfaits
5. BLOCKED : Bloquée par une dépendance

**Éléments constitutifs :**

- Nom court
- Rôle utilisateur
- Fonctionnalité désirée
- Bénéfice attendu
- Priorité
- Critères d'acceptation
- Tâches techniques

### Sprint Backlog

**Définition :** Ensemble des User Stories sélectionnées depuis le Product Backlog pour être réalisées durant un Sprint spécifique.

**Contenu :**

- User Stories du Sprint
- Plan pour atteindre l'objectif du Sprint
- Tâches identifiées

**Responsable :** Équipe de développement

**Caractéristiques :**

- Appartient à l'équipe
- Modifiable durant le Sprint (ajout de tâches)
- Fige les User Stories (pas d'ajout pendant le Sprint)

### Task (Tâche)

**Définition :** Unité de travail technique atomique décomposée depuis une User Story.

**Exemples :**

- "Créer le formulaire de connexion HTML"
- "Implémenter l'endpoint POST /api/login"
- "Écrire les tests unitaires du service d'authentification"
- "Mettre à jour la documentation API"

**Caractéristiques :**

- Technique et concrète
- Estimée en heures
- Assignée à un développeur
- Durée : quelques heures à 1 jour maximum

**Statuts :**

- TODO : À faire
- IN_PROGRESS : En cours
- DONE : Terminée
- BLOCKED : Bloquée

### Acceptance Criteria (Critère d'acceptation)

**Définition :** Conditions qui doivent être satisfaites pour qu'une User Story soit considérée comme "Done".

**Format BDD (Behavior-Driven Development) :**

```
GIVEN [contexte/état initial]
WHEN [action/événement]
THEN [résultat attendu]
```

**Exemple :**

```
GIVEN l'utilisateur est sur la page de connexion
WHEN il entre un email et mot de passe valides
THEN il est redirigé vers son tableau de bord
```

**Caractéristiques :**

- Testable et vérifiable
- Sans ambiguïté
- Indépendant de l'implémentation
- Orienté résultat utilisateur

**Importance :**

- Définit quand la story est "Done"
- Base pour les tests d'acceptation
- Critère de recette avec le Product Owner

## 🎯 Concepts et statuts

### Definition of Done (DoD)

Liste de critères qu'une User Story doit satisfaire pour être considérée complète.

**Exemple de DoD :**

- ✅ Code écrit et testé
- ✅ Tests unitaires écrits et passent
- ✅ Revue de code effectuée
- ✅ Documentation mise à jour
- ✅ Tous les critères d'acceptation validés
- ✅ Déployé en environnement de test

### Story Points

Unité de mesure abstraite pour estimer l'effort relatif nécessaire pour implémenter une User Story.

**Échelle courante :** Suite de Fibonacci (1, 2, 3, 5, 8, 13, 21)

**Facteurs considérés :**

- Complexité technique
- Quantité de travail
- Incertitude/risque

### Technical Debt (Dette technique)

Coût futur causé par des choix de développement rapides au détriment de solutions meilleures mais plus longues.

### Refactoring

Restructuration du code pour améliorer sa qualité sans changer son comportement externe.

### Backlog Grooming/Refinement

Activité continue d'affinage du Product Backlog : ajout de détails, estimations, priorisation.

## 🏗️ Architecture et code

### DTO (Data Transfer Object)

Objet utilisé pour transférer des données entre couches de l'application.

**Types dans APMS :**

- **Request DTO** : Données entrantes (création/modification)
- **Response DTO** : Données sortantes (vers le client)

**Avantages :**

- Isolation du modèle de domaine
- Validation centralisée
- Transformation contrôlée
- Sécurité (pas d'exposition directe des entités)

### Entity (Entité)

Objet du modèle de domaine représentant une table en base de données.

**Caractéristiques :**

- Annotée avec `@Entity`
- Possède un identifiant (`@Id`)
- Mappée à une table
- Contient les relations JPA

### Mapper

Composant responsable de la conversion entre entités et DTOs.

**Technologie :** MapStruct (génération automatique à la compilation)

### Repository

Interface d'accès aux données héritant de `JpaRepository`.

**Responsabilités :**

- CRUD de base
- Requêtes personnalisées
- Gestion du cache de premier niveau

### Service

Couche contenant la logique métier de l'application.

**Types :**

- **Interface** : Contrat du service
- **Implementation** : Logique concrète

**Responsabilités :**

- Orchestration des opérations
- Application des règles métier
- Gestion transactionnelle
- Coordination des repositories et mappers

### Validator

Composant dédié à la validation de règles métier complexes.

**Exemples :**

- Vérifier que tous les critères d'acceptation sont satisfaits avant de marquer une story comme DONE
- Valider qu'une story peut être liée à un Epic (statut TODO requis)

### Helper

Composant utilitaire centralisant les opérations de recherche communes.

**Avantages :**

- Gestion uniforme des erreurs (ResourceNotFoundException)
- Réutilisation du code
- Cohérence

### Controller

Point d'entrée REST API exposant les endpoints HTTP.

**Responsabilités :**

- Recevoir les requêtes HTTP
- Valider les données entrantes
- Appeler le service approprié
- Retourner les réponses HTTP

### Aspect (AOP)

Code transversal appliqué automatiquement à plusieurs points du programme.

**Exemples dans APMS :**

- Logging automatique des opérations
- Mesure du temps d'exécution
- Gestion des transactions

## 🔧 Patterns de conception

### Repository Pattern

Abstraction de la couche de persistance pour isoler le reste de l'application.

### DTO Pattern

Utilisation d'objets de transfert pour découpler l'API du modèle interne.

### Service Layer Pattern

Encapsulation de la logique métier dans une couche dédiée.

### Dependency Injection (DI)

Fourniture des dépendances d'un objet de l'extérieur plutôt que de les créer en interne.

**Mécanisme :** Spring IoC Container

### Builder Pattern

Construction progressive d'objets complexes.

**Technologie :** Lombok `@Builder`

## 📊 Métriques et qualité

### Code Coverage (Couverture de code)

Pourcentage du code source testé par les tests automatisés.

**Outil :** JaCoCo

**Objectif APMS :** Minimum 80%

### Cyclomatic Complexity

Mesure de la complexité d'un programme basée sur le nombre de chemins d'exécution.

**Recommandation :** < 10 par méthode

### Technical Debt Ratio

Ratio entre le temps nécessaire pour corriger les problèmes de code et le temps de développement total.

**Outil :** SonarQube

### Lines of Code (LOC)

Nombre de lignes de code (métrique basique).

## 🔒 Sécurité

### Validation

Vérification que les données reçues respectent les contraintes attendues.

**Niveaux :**

1. Validation de format (@NotNull, @Size, etc.)
2. Validation personnalisée (@StartEndDateValidator)
3. Validation métier (Validators)

### Sanitization

Nettoyage des entrées pour prévenir les injections.

**Protection JPA :** Requêtes paramétrées automatiques

## 🔄 Gestion de version

### Semantic Versioning (SemVer)

Format : MAJOR.MINOR.PATCH

- **MAJOR** : Changements incompatibles
- **MINOR** : Nouvelles fonctionnalités compatibles
- **PATCH** : Corrections de bugs

### Git Flow

Stratégie de branchement Git structurée.

**Branches principales :**

- `main` : Code de production
- `develop` : Intégration continue

**Branches support :**

- `feature/` : Nouvelles fonctionnalités
- `bugfix/` : Corrections de bugs
- `hotfix/` : Corrections urgentes

## 📝 Documentation

### Javadoc

Documentation du code Java sous forme de commentaires spéciaux.

**Tags principaux :**

- `@param` : Paramètre de méthode
- `@return` : Valeur de retour
- `@throws` : Exception levée
- `@see` : Référence croisée
- `@since` : Version d'introduction
- `@author` : Auteur
- `@version` : Version

### API Documentation

Documentation des endpoints REST.

**Outils possibles :**

- Swagger/OpenAPI
- Postman Collections
- Markdown (comme ce document)

## 🚀 DevOps

### CI/CD (Continuous Integration/Continuous Deployment)

Pratiques d'intégration et de déploiement continus.

**Outils :** Jenkins, GitLab CI, GitHub Actions

### Docker

Plateforme de conteneurisation permettant d'empaqueter une application avec ses dépendances.

### Docker Compose

Outil pour définir et exécuter des applications Docker multi-conteneurs.

## 📌 Abréviations courantes

| Abréviation | Signification                     |
| ----------- | --------------------------------- |
| APMS        | Agile Project Management System   |
| API         | Application Programming Interface |
| REST        | Representational State Transfer   |
| CRUD        | Create, Read, Update, Delete      |
| DTO         | Data Transfer Object              |
| JPA         | Java Persistence API              |
| ORM         | Object-Relational Mapping         |
| AOP         | Aspect-Oriented Programming       |
| BDD         | Behavior-Driven Development       |
| TDD         | Test-Driven Development           |
| DI          | Dependency Injection              |
| IoC         | Inversion of Control              |
| UUID        | Universally Unique Identifier     |
| JSON        | JavaScript Object Notation        |
| HTTP        | HyperText Transfer Protocol       |
| SQL         | Structured Query Language         |
| DoD         | Definition of Done                |
| US          | User Story                        |
| AC          | Acceptance Criteria               |
| PB          | Product Backlog                   |
| SB          | Sprint Backlog                    |

---

**Glossaire - Version 1.0**
_Dernière mise à jour : Décembre 2025_
