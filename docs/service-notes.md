# Notes sur la couche Service — Atelier 4

## A. Injection de dépendances

### 1. Injection par constructeur

L'injection par constructeur consiste à fournir les dépendances nécessaires à une classe lors de sa création.

Dans notre projet, les services utilisent `@RequiredArgsConstructor` de Lombok. Cette annotation génère un constructeur pour les attributs déclarés `final`.

Exemple :

```java
@Service
@RequiredArgsConstructor
public class ContratServiceImpl implements IContratService {

    private final ContratRepository contratRepository;
}
```

Spring utilise ce constructeur pour fournir automatiquement le repository nécessaire au service.

### 2. Comparaison avec l'injection par attribut

| Critère | Injection par constructeur | Injection par attribut |
|---|---|---|
| Attribut `final` | Possible | Généralement impossible |
| Dépendance visible | Oui, dans le constructeur | Moins visible |
| Objet créé sans Spring | Le constructeur exige la dépendance | L'attribut injecté peut rester `null` |
| Qualité du code | Favorise un code clair et testable | Peut rendre les tests plus difficiles |
| Recommandation | Préférée | À éviter dans ce projet |

L'injection par constructeur rend les dépendances obligatoires et facilite les tests unitaires.

## B. Organisation des services

Chaque service représente la couche métier de l'application. Il utilise un repository pour accéder aux données.

| Service | Dépendance | Mode | Justification |
|---|---|---|---|
| ContratServiceImpl | ContratRepository | Injection par constructeur | Gérer les contrats et vérifier le montant total |
| PaiementServiceImpl | PaiementRepository | Injection par constructeur | Consulter les paiements en lecture seule |
| AgenceServiceImpl | AgenceRepository | Injection par constructeur | Gérer les agences et vérifier leur nom |
| EmployeServiceImpl | EmployeRepository | Injection par constructeur | Gérer les employés et vérifier leur nom |
| VehiculeServiceImpl | VehiculeRepository | Injection par constructeur | Gérer les véhicules et interdire un tarif négatif |
| EquipementServiceImpl | EquipementRepository | Injection par constructeur | Gérer les équipements et vérifier leur libellé |
| ClientServiceImpl | ClientRepository | Injection par constructeur | Gérer les clients et vérifier les informations obligatoires |
| ReservationServiceImpl | ReservationRepository | Injection par constructeur | Gérer les réservations et vérifier les dates |
| MaintenanceServiceImpl | MaintenanceRepository | Injection par constructeur | Gérer les maintenances et vérifier les dates |

Les services de gestion proposent les opérations nécessaires de création, consultation, modification et suppression. Le service Paiement est une exception : il permet uniquement la consultation.

## C. Problèmes d'injection de dépendances

### 1. Bean IContratService introuvable

**Cause possible :** Spring ne trouve aucune implémentation du service.

**Solution :** vérifier que `ContratServiceImpl` possède l'annotation `@Service`, implémente `IContratService` et se trouve dans un package analysé par Spring.

### 2. Plusieurs beans pour une même interface

**Problème :** si plusieurs classes implémentent une même interface, Spring peut ne pas savoir laquelle injecter.

**Solutions possibles :**
- Utiliser `@Primary` sur l'implémentation à privilégier.
- Utiliser `@Qualifier` pour préciser le bean voulu au point d'injection.

Exemple :

```java
public MonService(
        @Qualifier("emailNotificateur") Notificateur notificateur) {
    this.notificateur = notificateur;
}
```

Cet exemple suppose que le bean `emailNotificateur` existe et que les autres éléments du constructeur sont déclarés.

### 3. Dépendances circulaires

Une dépendance circulaire apparaît lorsque deux services dépendent directement l'un de l'autre.

Par exemple, `ClientService` dépend de `ReservationService` et `ReservationService` dépend de `ClientService`.

Cette situation complique la création des beans et peut empêcher le démarrage de l'application.

**Solution :** supprimer la dépendance inutile ou déplacer la responsabilité commune vers un service indépendant.

## D. Anomalies SonarQube et corrections

### 1. Injection par attribut

Une injection directe par attribut, par exemple avec `@Autowired`, peut être signalée par SonarQube.

**Correction :** préférer l'injection par constructeur. Dans notre projet, `@RequiredArgsConstructor` et les attributs `final` permettent de suivre cette approche.

### 2. Exceptions trop générales

L'utilisation de `RuntimeException` directement peut être signalée, car elle ne décrit pas précisément l'erreur.

**Correction :** utiliser une exception adaptée, comme `IllegalArgumentException` pour un argument invalide ou `ResourceNotFoundException` pour une ressource absente.

### 3. Imports inutilisés

Un import qui n'est jamais utilisé rend le code moins propre et peut déclencher une anomalie de qualité.

**Correction :** supprimer les imports inutiles et utiliser la fonction d'optimisation des imports d'IntelliJ.

Ces corrections améliorent la lisibilité, la maintenabilité et la qualité du code.

## E. Questions de compréhension

### 1. Qui instancie `ContratServiceImpl` ?

C'est le conteneur Spring qui crée et gère l'objet. Grâce à `@Service`, Spring détecte la classe pendant le scan des composants et prépare son instanciation avec les dépendances nécessaires.

### 2. Pourquoi un contrôleur doit-il dépendre de `IContratService` plutôt que de `ContratServiceImpl` ?

Le contrôleur dépend de l'interface, qui définit les opérations disponibles. Cela réduit le couplage avec l'implémentation et permet de remplacer cette dernière plus facilement.

### 3. Pourquoi un service singleton ne doit-il pas contenir un état mutable propre à une requête ?

Un service Spring est généralement un singleton : la même instance peut être utilisée par plusieurs requêtes. Stocker des données propres à un utilisateur ou à une requête dans ses attributs peut provoquer des mélanges entre requêtes.

Les données temporaires doivent donc être conservées dans les variables locales des méthodes ou dans des objets adaptés.

### 4. Pourquoi `update()` recherche-t-il d'abord l'entité existante ?

Cette méthode récupère l'entité déjà enregistrée, puis modifie ses attributs. Cela permet de conserver son identifiant et d'éviter de remplacer involontairement des relations ou des collections, comme les paiements associés à un contrat.

### 5. Quelle est la différence entre `@Component` et `@Bean` ?

`@Component` est placée sur une classe pour que Spring puisse la détecter automatiquement lors du scan des composants. `@Service` est une spécialisation de `@Component`.

`@Bean` est placée sur une méthode d'une classe de configuration. L'objet retourné par cette méthode est alors enregistré dans le conteneur Spring.

### 6. Quelle est la différence entre `@Primary` et `@Qualifier` ?

`@Primary` indique à Spring quelle implémentation privilégier lorsqu'il existe plusieurs beans compatibles.

`@Qualifier` permet de désigner explicitement le bean souhaité au point d'injection.

### 7. Pourquoi `PaiementServiceImpl` est-il en lecture seule ?

Dans notre projet, le service Paiement expose uniquement `findById()` et `findAll()`. Il permet donc de consulter les paiements sans proposer directement leur création, leur modification ou leur suppression depuis ce service.

## Conclusion

La couche Service centralise les règles métier et utilise les repositories pour accéder aux données. L'injection par constructeur, la validation des données et la gestion des exceptions contribuent à rendre l'application plus fiable et plus facile à maintenir.
