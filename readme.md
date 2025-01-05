# 🏦 Application CQRS & Event Sourcing pour Gestion Bancaire

![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.1-brightgreen)
![Axon Framework](https://img.shields.io/badge/Axon-4.10.3-blue)
![MySQL](https://img.shields.io/badge/MySQL-8.0-orange)

## 📝 Description

Application bancaire implémentant CQRS (Command Query Responsibility Segregation) et Event Sourcing pour la gestion des comptes et opérations bancaires.

## 🏗️ Architecture

### Command Side

- Gestion des commandes de modification d'état
- Pattern Agrégat pour la logique métier
- Event Sourcing pour la traçabilité

```java
// Command Example
public class CreateAccountCommand extends BaseCommand<String> {
    private double initialBalance;
    private String currency;
}

// Aggregate Example
@Aggregate
public class AccountAggregate {
    @AggregateIdentifier
    private String accountId;
    private double balance;

    @CommandHandler
    public AccountAggregate(CreateAccountCommand cmd) {
        if(cmd.getInitialBalance() < 0)
            throw new IllegalArgumentException("Solde initial invalide");
        AggregateLifecycle.apply(new AccountCreatedEvent(
            cmd.getId(),
            cmd.getInitialBalance(),
            cmd.getCurrency()
        ));
    }
}
```

### Query Side

- Lecture des données
- Projections optimisées
- Synchronisation via Event Handlers

```java
@Entity
public class Account {
    @Id
    private String id;
    private double solde;
    private String currency;
    @Enumerated(EnumType.STRING)
    private AccountStatus status;
    @OneToMany(mappedBy = "account")
    private Collection<Operation> operations;
}

@Service
@RequiredArgsConstructor
public class AccountServiceHandler {
    private final AccountRepository accountRepository;

    @EventHandler
    public void on(AccountCreatedEvent event) {
        Account account = Account.builder()
            .id(event.getId())
            .solde(event.getInitialBalance())
            .currency(event.getCurrency())
            .status(AccountStatus.CREATED)
            .build();
        accountRepository.save(account);
    }
}
```

### Configuration

```properties
# application.properties
spring.datasource.url=jdbc:mysql://localhost:3306/bank_db
spring.datasource.username=root
spring.datasource.password=root
spring.jpa.hibernate.ddl-auto=update
```

### REST Controllers

```java
@RestController
@RequestMapping("/commands/account")
public class AccountCommandController {
    @PostMapping("/create")
    public CompletableFuture<String> createAccount(@RequestBody CreateAccountDTO request) {
        // Implementation
    }

    @PutMapping("/credit")
    public CompletableFuture<String> creditAccount(@RequestBody CreditAccountDTO request) {
        // Implementation
    }

    @PutMapping("/debit")
    public CompletableFuture<String> debitAccount(@RequestBody DebitAccountDTO request) {
        // Implementation
    }
}

@RestController
@RequestMapping("/query/accounts")
public class AccountQueryController {
    @GetMapping
    public List<AccountDTO> getAllAccounts() {
        // Implementation
    }

    @GetMapping("/{id}")
    public AccountDTO getAccount(@PathVariable String id) {
        // Implementation
    }
}
```

## 🛠️ Utilisation

```bash
# Cloner le projet
git clone https://github.com/YounesJabbour/CQRS_Event_Sourcing_bank_app

# Installer les dépendances
mvn clean install

# Démarrer l'application
mvn spring-boot:run
```

## 📂 Structure du Projet

```
src/
├── main/
│   ├── java/
│   │   └── app/
│   │       ├── commands/
│   │       ├── commonApi/
│   │       └── query/
│   └── resources/
└── test/
    └── java/
        └── app/
            ├── commands/
            ├── commonApi/
            └── query/
```

## 🧪 Tests

```bash
# Exécuter les tests
mvn test

# Tests d'intégration
mvn verify
```

## 📜 Enums et DTOs

```java
public enum AccountStatus {
    CREATED, ACTIVATED, SUSPENDED
}

public enum OperationType {
    DEBIT, CREDIT
}

public record CreateAccountDTO(
    Double initialBalance,
    String currency
) {}

public record AccountDTO(
    String id,
    double balance,
    String currency,
    AccountStatus status
) {}
```

## 🛠️ Technologies Utilisées

- Spring Boot 3.4.1
- Axon Framework 4.10.3
- MySQL 8.0
- JPA/Hibernate
- Lombok
- Maven
