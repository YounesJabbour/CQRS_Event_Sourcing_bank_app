package app.cqrs_event_sourcing_app.query.service;

import app.cqrs_event_sourcing_app.commonApi.enums.AccountStatus;
import app.cqrs_event_sourcing_app.commonApi.enums.OperationStatus;
import app.cqrs_event_sourcing_app.commonApi.events.AccountActivatedEvent;
import app.cqrs_event_sourcing_app.commonApi.events.AccountCreatedEvent;
import app.cqrs_event_sourcing_app.commonApi.events.AccountCreditedEvent;
import app.cqrs_event_sourcing_app.commonApi.events.AccountDebitedEvent;
import app.cqrs_event_sourcing_app.commonApi.queries.GetAccountByIdQuery;
import app.cqrs_event_sourcing_app.commonApi.queries.GetAllAccountsQuery;
import app.cqrs_event_sourcing_app.exceptions.AccountNotFoundException;
import app.cqrs_event_sourcing_app.query.entities.Account;
import app.cqrs_event_sourcing_app.query.entities.Operation;
import app.cqrs_event_sourcing_app.query.repository.AccountRepository;
import app.cqrs_event_sourcing_app.query.repository.OperationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountServiceHandler {
    private final AccountRepository accountRepository;
    private final OperationRepository operationRepository;

    @EventHandler
    public void on(AccountCreatedEvent event) {
        log.info("==================================================");
        log.info("Account Created event created received: {}", event.getId());
        log.info("==================================================");
        accountRepository.save(Account.builder()
                .id(event.getId())
                .solde(event.getInitialBalance())
                .currency(event.getCurrency())
                .status(AccountStatus.CREATED)
                .operationList(null)
                .build());
    }

    @EventHandler
    public void on(AccountActivatedEvent event) {
        log.info("==================================================");
        log.info("Account activated event received: {}", event.getId());
        log.info("==================================================");
        Account account = accountRepository.findById(event.getId())
                .orElseThrow(() -> new AccountNotFoundException("Account not found"));
        account.setStatus(AccountStatus.ACTIVATED);
        accountRepository.save(account);
    }

    @EventHandler
    public void on(AccountDebitedEvent event) {
        log.info("==================================================");
        log.info("Account debited event received: {}", event.getId());
        log.info("==================================================");

        Account account = accountRepository.findById(event.getId())
                .orElseThrow(() -> new AccountNotFoundException("Account not found"));
        Operation operation = Operation.builder()
                .date(event.getDate())
                .status(OperationStatus.DEBIT)
                .account(account)
                .build();
        account.setSolde(account.getSolde() - event.getAmount());
        operationRepository.save(operation);
        accountRepository.save(account);
    }

    @EventHandler
    public void on(AccountCreditedEvent event) {
        log.info("==================================================");
        log.info("Account credited event received: {}", event.getId());
        log.info("==================================================");
        Account account = accountRepository.findById(event.getId())
                .orElseThrow(() -> new AccountNotFoundException("Account not found"));
        Operation operation = Operation.builder()
                .date(event.getDate())
                .status(OperationStatus.CREDIT)
                .account(account)
                .build();
        account.setSolde(account.getSolde() + event.getAmount());
        operationRepository.save(operation);
        accountRepository.save(account);
    }

    @QueryHandler
    public List<Account> on(GetAllAccountsQuery query) {
        return accountRepository.findAll();
    }
    @QueryHandler
    public Account on(GetAccountByIdQuery query) {
        return accountRepository.findById(query.getId())
                .orElseThrow(() -> new AccountNotFoundException("Account not found"));
    }
}
