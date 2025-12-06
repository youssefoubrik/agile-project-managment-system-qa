# Index de la Documentation - APMS

Bienvenue dans la documentation complète du système APMS (Agile Project Management System). Ce document vous guide vers les différentes ressources disponibles.

## 📚 Documentation disponible

### 🏠 Documents principaux

1. **[README.md](README.md)** - Point d'entrée principal

   - Vue d'ensemble du projet
   - Installation et configuration
   - Guide de démarrage rapide
   - Technologies utilisées
   - Structure du projet

2. **[ARCHITECTURE.md](ARCHITECTURE.md)** - Documentation d'architecture

   - Architecture en couches détaillée
   - Patterns et principes de conception
   - Aspects transversaux (AOP)
   - Validation et transactions
   - Performance et déploiement

3. **[API_GUIDE.md](API_GUIDE.md)** - Guide complet de l'API REST

   - Documentation de tous les endpoints
   - Exemples de requêtes/réponses
   - Codes d'erreur
   - Flux d'utilisation complets

4. **[CONTRIBUTING.md](CONTRIBUTING.md)** - Guide de contribution

   - Standards de code
   - Conventions de nommage
   - Process de Pull Request
   - Guide des commits

5. **[GLOSSARY.md](GLOSSARY.md)** - Glossaire et terminologie
   - Concepts Agile/Scrum
   - Entités du domaine
   - Termes techniques
   - Abréviations

## 🎯 Par profil utilisateur

### Vous êtes nouveau sur le projet ?

1. Commencez par le [README.md](README.md) pour comprendre le projet
2. Suivez les instructions d'[installation](README.md#installation)
3. Consultez le [GLOSSARY.md](GLOSSARY.md) pour la terminologie
4. Explorez l'[API_GUIDE.md](API_GUIDE.md) pour utiliser l'API

### Vous êtes développeur et voulez contribuer ?

1. Lisez le [CONTRIBUTING.md](CONTRIBUTING.md) en entier
2. Consultez [ARCHITECTURE.md](ARCHITECTURE.md) pour comprendre la structure
3. Suivez les standards de code définis
4. Créez une branche et soumettez une Pull Request

### Vous êtes architecte ou tech lead ?

1. Étudiez [ARCHITECTURE.md](ARCHITECTURE.md) pour la conception
2. Consultez les patterns et principes utilisés
3. Revoyez les décisions d'architecture

### Vous utilisez l'API ?

1. Consultez [API_GUIDE.md](API_GUIDE.md) pour tous les endpoints
2. Testez avec les exemples fournis
3. Référez-vous au [GLOSSARY.md](GLOSSARY.md) pour les concepts métier

## 📖 Par sujet

### Installation et configuration

- [README.md - Prérequis](README.md#prérequis)
- [README.md - Installation](README.md#installation)
- [README.md - Configuration](README.md#configuration)
- [README.md - Lancement](README.md#lancement-de-lapplication)

### Architecture et conception

- [ARCHITECTURE.md - Vue d'ensemble](ARCHITECTURE.md#vue-densemble)
- [ARCHITECTURE.md - Couches](ARCHITECTURE.md#architecture-en-couches)
- [ARCHITECTURE.md - Patterns](ARCHITECTURE.md#patterns-et-principes)
- [ARCHITECTURE.md - AOP](ARCHITECTURE.md#aspects-transversaux-aop)

### API REST

- [API_GUIDE.md - Projects](API_GUIDE.md#projects)
- [API_GUIDE.md - Epics](API_GUIDE.md#epics)
- [API_GUIDE.md - User Stories](API_GUIDE.md#user-stories)
- [API_GUIDE.md - Tasks](API_GUIDE.md#tasks)
- [API_GUIDE.md - Acceptance Criteria](API_GUIDE.md#acceptance-criteria)
- [API_GUIDE.md - Sprint Backlogs](API_GUIDE.md#sprint-backlogs)

### Standards et bonnes pratiques

- [CONTRIBUTING.md - Standards de code](CONTRIBUTING.md#standards-de-code)
- [CONTRIBUTING.md - Conventions de nommage](CONTRIBUTING.md#conventions-de-nommage)
- [CONTRIBUTING.md - Tests](CONTRIBUTING.md#tests)
- [CONTRIBUTING.md - Documentation](CONTRIBUTING.md#documentation)

### Concepts métier

- [GLOSSARY.md - Méthodologie Agile](GLOSSARY.md#méthodologie-agilescrum)
- [GLOSSARY.md - Entités du domaine](GLOSSARY.md#entités-du-domaine)
- [GLOSSARY.md - Concepts et statuts](GLOSSARY.md#concepts-et-statuts)

### Code source

- [Structure du projet](README.md#structure-du-projet)
- [Javadoc dans le code](src/main/java/ma/ensa/apms/)

## 🔍 Recherche rapide

### Je veux...

**...installer le projet**
→ [README.md - Installation](README.md#installation)

**...comprendre l'architecture**
→ [ARCHITECTURE.md](ARCHITECTURE.md)

**...utiliser l'API**
→ [API_GUIDE.md](API_GUIDE.md)

**...contribuer au projet**
→ [CONTRIBUTING.md](CONTRIBUTING.md)

**...comprendre un terme**
→ [GLOSSARY.md](GLOSSARY.md)

**...créer un projet avec des user stories**
→ [API_GUIDE.md - Exemples de flux complets](API_GUIDE.md#exemples-de-flux-complets)

**...exécuter les tests**
→ [README.md - Tests](README.md#tests)

**...déployer l'application**
→ [ARCHITECTURE.md - Déploiement](ARCHITECTURE.md#déploiement)

## 📊 Structure de la documentation

```
documentation/
├── README.md               # Vue d'ensemble et démarrage rapide
├── ARCHITECTURE.md         # Architecture technique détaillée
├── API_GUIDE.md           # Documentation complète de l'API REST
├── CONTRIBUTING.md        # Guide de contribution
├── GLOSSARY.md           # Glossaire et terminologie
└── DOCUMENTATION_INDEX.md # Ce fichier (index)

code-source/
└── src/
    └── main/java/ma/ensa/apms/
        ├── controller/    # Javadoc des contrôleurs
        ├── service/       # Javadoc des services
        ├── modal/         # Javadoc des entités
        └── ...           # Autres packages documentés
```

## 🔄 Mise à jour de la documentation

La documentation est maintenue à jour avec le code. Pour chaque modification :

1. **Nouvelle fonctionnalité** → Mettre à jour :

   - API_GUIDE.md (si nouveaux endpoints)
   - README.md (si changement de structure)
   - GLOSSARY.md (si nouveaux concepts)

2. **Modification d'architecture** → Mettre à jour :

   - ARCHITECTURE.md
   - README.md (structure du projet si nécessaire)

3. **Changement de process** → Mettre à jour :
   - CONTRIBUTING.md

## 📝 Conventions de documentation

### Format

- **Markdown** pour tous les documents
- **Javadoc** pour le code Java
- **Commentaires inline** pour la logique complexe

### Structure des documents

- Titre principal (H1)
- Table des matières
- Sections avec titres (H2, H3)
- Exemples de code
- Liens croisés entre documents

### Style

- Langage clair et concis
- Exemples concrets
- Diagrammes si nécessaire
- Émojis pour la navigation visuelle

## 🆘 Besoin d'aide ?

### Documentation manquante ou incomplète ?

Ouvrez une issue GitHub avec le tag `documentation`

### Question sur l'utilisation ?

1. Consultez le [GLOSSARY.md](GLOSSARY.md)
2. Cherchez dans [API_GUIDE.md](API_GUIDE.md)
3. Ouvrez une issue avec le tag `question`

### Bug trouvé dans la documentation ?

1. Identifiez le document concerné
2. Ouvrez une Pull Request avec la correction
3. Suivez les [guidelines de contribution](CONTRIBUTING.md)

## 📈 Évolution de la documentation

### Version actuelle : 1.0

**Dernière mise à jour :** Décembre 2025

**Changements récents :**

- ✅ Documentation initiale complète
- ✅ README avec guide d'installation
- ✅ Architecture détaillée
- ✅ API Guide complet
- ✅ Guide de contribution
- ✅ Glossaire Agile et technique

### Prochaines améliorations prévues

- [ ] Diagrammes UML de l'architecture
- [ ] Vidéos tutorielles
- [ ] Collection Postman exportable
- [ ] FAQ (Questions fréquentes)
- [ ] Guide de troubleshooting
- [ ] Documentation des tests
- [ ] Guide de performance tuning
- [ ] Documentation Swagger/OpenAPI intégrée

## 🔗 Liens externes utiles

### Technologies utilisées

- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [Spring Data JPA](https://spring.io/projects/spring-data-jpa)
- [MapStruct](https://mapstruct.org/)
- [Lombok](https://projectlombok.org/)
- [PostgreSQL Documentation](https://www.postgresql.org/docs/)

### Méthodologie Agile

- [Scrum Guide](https://scrumguides.org/)
- [Agile Manifesto](https://agilemanifesto.org/)
- [User Story Mapping](https://www.jpattonassociates.com/user-story-mapping/)

### Standards et bonnes pratiques

- [Java Code Conventions](https://www.oracle.com/java/technologies/javase/codeconventions-contents.html)
- [REST API Best Practices](https://restfulapi.net/)
- [Conventional Commits](https://www.conventionalcommits.org/)
- [Semantic Versioning](https://semver.org/)

## 📧 Contact

Pour toute question sur la documentation :

- Ouvrir une issue GitHub
- Tag : `documentation`, `question`, ou `help wanted`

---

**Index de la Documentation - Version 1.0**
_Dernière mise à jour : Décembre 2025_

**Fait avec ❤️ pour faciliter la collaboration**
