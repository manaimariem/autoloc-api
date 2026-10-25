# Atelier 3 — Spring Data JPA et Repository

## 1. Objectif

L'objectif de cet atelier est d'utiliser Spring Data JPA pour accéder aux données de l'application AutoLoc à travers des interfaces Repository.

Spring Data JPA permet de réaliser les opérations CRUD sans avoir besoin d'écrire manuellement les requêtes SQL de base.

CRUD signifie :
- **Create** : créer une entité.
- **Read** : lire ou rechercher une entité.
- **Update** : modifier une entité.
- **Delete** : supprimer une entité.

## 2. Package Repository

Les interfaces Repository se trouvent dans le package :

`tn.esprit.autoloc.repository`

Chaque interface étend `JpaRepository`, qui fournit les méthodes nécessaires pour accéder aux données de la base de données.

Le type `Long` correspond au type de l'identifiant de chaque entité.

## 3. Interfaces Repository et justifications

Le projet AutoLoc contient neuf interfaces Repository.

| Interface | Entité associée | Extension | Justification |
|---|---|---|---|
| `IAgenceRepository` | `Agence` | `JpaRepository<Agence, Long>` | Gérer les agences. |
| `IEmployeRepository` | `Employe` | `JpaRepository<Employe, Long>` | Gérer les employés. |
| `IVehiculeRepository` | `Vehicule` | `JpaRepository<Vehicule, Long>` | Gérer les véhicules. |
| `IEquipementRepository` | `Equipement` | `JpaRepository<Equipement, Long>` | Gérer les équipements. |
| `IClientRepository` | `Client` | `JpaRepository<Client, Long>` | Gérer les clients. |
| `IReservationRepository` | `Reservation` | `JpaRepository<Reservation, Long>` | Gérer les réservations. |
| `IContratRepository` | `Contrat` | `JpaRepository<Contrat, Long>` | Gérer les contrats. |
| `IPaiementRepository` | `Paiement` | `JpaRepository<Paiement, Long>` | Accéder aux données des paiements. |
| `IMaintenanceRepository` | `Maintenance` | `JpaRepository<Maintenance, Long>` | Gérer les maintenances. |

Ces interfaces permettent de séparer l'accès aux données du reste de l'application.

## 4. Opérations disponibles

Grâce à `JpaRepository`, plusieurs méthodes sont disponibles.

- `save()` : enregistrer une nouvelle entité ou sauvegarder les modifications d'une entité.
- `findById()` : rechercher une entité par son identifiant. Le résultat est un `Optional`.
- `findAll()` : récupérer la liste des entités.
- `existsById()` : vérifier si une entité existe.
- `deleteById()` : supprimer une entité par son identifiant.
- `count()` : compter le nombre d'entités.
- `deleteAll()` : supprimer toutes les entités.

### Remarques importantes

- `findById()` retourne un `Optional`, car l'entité recherchée peut ne pas exister.
- `findAll()` retourne une `List` lorsqu'elle est appelée depuis `JpaRepository`.
- Pour une suppression nécessitant des vérifications métier, il est préférable de vérifier l'existence de l'entité avant de la supprimer.
- Les suppressions en lot, comme `deleteAllInBatch()`, peuvent contourner certaines opérations du cycle de vie des entités et les cascades JPA.

## 5. Injection et utilisation des Repository

Les interfaces Repository sont gérées par Spring. Il n'est donc pas nécessaire de créer manuellement leurs instances avec `new`.

Spring détecte les interfaces Repository et fournit automatiquement les implémentations nécessaires.

Dans la couche Service, les Repository sont injectés par constructeur afin de respecter les bonnes pratiques de dépendance et de faciliter les tests.

## 6. Vérification de l'application

Au démarrage de l'application, Spring Boot détecte les neuf interfaces Repository.

Le message attendu dans les logs est :

`Found 9 JPA repository interfaces`

La connexion à la base de données MySQL `autoloc_db` a également été vérifiée au démarrage.

Les logs affichent notamment la connexion à MySQL et le démarrage de l'application sur le port 8081.

## 7. Analyse statique avec SonarQube for IDE

SonarQube for IDE a été installé et utilisé dans IntelliJ IDEA pour analyser les fichiers Java du projet.

Les fichiers vérifiés comprennent notamment :
- `IAgenceRepository.java`
- `IVehiculeRepository.java`
- `Vehicule.java`
- `Client.java`
- `Contrat.java`
- `Agence.java`

L'analyse des fichiers Java vérifiés a affiché le résultat `No Issues` ou `No findings to display`.

Cela signifie qu'aucune anomalie n'a été signalée pour les fichiers contrôlés lors de cette vérification.

Aucune anomalie SonarQube corrigée n'est recensée dans ce compte rendu, car aucune anomalie n'a été confirmée parmi les résultats observés.

## 8. Conclusion

L'atelier a permis de mettre en place la couche Repository de l'application AutoLoc avec Spring Data JPA.

Les neuf interfaces Repository étendent `JpaRepository` et permettent d'accéder aux données sans écrire manuellement les opérations CRUD de base.

Le démarrage de l'application confirme la détection des neuf interfaces Repository et la connexion à la base de données MySQL.

L'analyse SonarQube for IDE des fichiers vérifiés n'a signalé aucun problème.