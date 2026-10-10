# Atelier 2 — Associations JPA : Cascade et Fetch

## 1. Objectif

L'objectif de cet atelier est de définir les associations entre les entités JPA du projet AutoLoc, de choisir les stratégies de chargement et de configurer les cascades en fonction du cycle de vie des objets.

## 2. Tableau récapitulatif des associations

| Association | Type | Côté propriétaire | Fetch | Cascade |
|---|---|---|---|---|
| Contrat – Paiement | OneToMany / ManyToOne | Paiement | LAZY | ALL + orphanRemoval = true côté Contrat |
| Agence – Vehicule | OneToMany / ManyToOne | Vehicule | LAZY | Aucune |
| Agence – Employe | OneToMany / ManyToOne | Employe | LAZY | Aucune |
| Vehicule – Equipement | ManyToMany | Vehicule | LAZY | Aucune |
| Client – Reservation | OneToMany / ManyToOne | Reservation | LAZY | PERSIST côté Client |
| Reservation – Vehicule | ManyToOne | Reservation | LAZY | Aucune |
| Reservation – Contrat | OneToOne | Contrat | LAZY | ALL côté Reservation |
| Vehicule – Maintenance | OneToMany / ManyToOne | Maintenance | LAZY | PERSIST côté Vehicule |

## 3. Justification des stratégies de chargement

### FetchType.LAZY

La stratégie LAZY permet de charger une entité associée ou une collection uniquement lorsqu'elle est utilisée. Elle évite de charger inutilement toutes les données associées et contribue à améliorer les performances.

Dans cet atelier, LAZY est utilisé pour les associations conformément aux recommandations du modèle AutoLoc.

### Comparaison entre LAZY et EAGER

Avec LAZY, les données associées sont chargées lorsque l'application y accède.

Avec EAGER, les données associées doivent être chargées immédiatement selon le contrat JPA, même si elles ne sont pas utilisées directement.

EAGER peut simplifier l'accès aux données associées, mais peut aussi augmenter le coût des lectures. Pour AutoLoc, LAZY est retenu afin de limiter le chargement inutile des associations.

## 4. Justification des cascades

### Contrat – Paiement : CascadeType.ALL et orphanRemoval

Un paiement dépend de son contrat. CascadeType.ALL propage les opérations du cycle de vie du contrat à ses paiements associés, notamment la suppression.

orphanRemoval = true supprime un paiement lorsqu'il est retiré de la collection paiements du contrat et que cette modification est persistée.

### Client – Reservation : CascadeType.PERSIST

CascadeType.PERSIST permet de propager l'enregistrement d'un nouveau client à ses réservations associées lorsqu'elles sont nouvelles.

### Vehicule – Maintenance : CascadeType.PERSIST

CascadeType.PERSIST permet de propager l'enregistrement d'un véhicule à ses nouvelles opérations de maintenance associées.

### Reservation – Contrat : CascadeType.ALL

La cascade est configurée côté Reservation conformément aux consignes de l'atelier. Elle propage les opérations du cycle de vie de la réservation vers le contrat associé.

### Associations sans cascade

Aucune cascade n'est configurée pour Agence – Vehicule, Agence – Employe, Vehicule – Equipement et Reservation – Vehicule.

Ces associations ne doivent pas entraîner automatiquement la propagation de toutes les opérations du cycle de vie. En particulier, un véhicule peut survivre à la suppression de son agence et les équipements peuvent être partagés entre plusieurs véhicules.

## 5. Vérifications du schéma

Les journaux Hibernate ont confirmé les opérations suivantes :

- Ajout de la clé étrangère id_agence dans la table vehicule.
- Ajout de la clé étrangère id_agence dans la table employe.
- Présence de la table de liaison vehicule_equipement avec les colonnes id_vehicule et id_equipement.
- Ajout de la clé étrangère id_client dans la table reservation.
- Ajout de la clé étrangère id_vehicule dans la table reservation.
- Ajout de la clé étrangère id_reservation dans la table contrat, avec une contrainte UNIQUE.
- Ajout de la clé étrangère id_vehicule dans la table maintenance.

L'application Spring Boot a démarré correctement après l'ajout des associations.

## 6. Conclusion

Les associations JPA du modèle AutoLoc ont été définies en respectant les multiplicités, les côtés propriétaires et inverses, les stratégies de chargement et les cascades demandées dans l'atelier.

Le choix de LAZY limite le chargement automatique des données associées. Les cascades sont utilisées uniquement pour les associations où la propagation du cycle de vie est prévue par les consignes.