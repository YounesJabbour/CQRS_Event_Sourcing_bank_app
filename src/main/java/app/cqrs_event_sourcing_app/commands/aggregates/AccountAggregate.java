package app.cqrs_event_sourcing_app.commands.aggregates;


import app.cqrs_event_sourcing_app.commonApi.commands.CreateAccountCommand;
import app.cqrs_event_sourcing_app.commonApi.commands.CreditAccountCommand;
import app.cqrs_event_sourcing_app.commonApi.commands.DebitAccountCommand;
import app.cqrs_event_sourcing_app.commonApi.enums.AccountStatus;
import app.cqrs_event_sourcing_app.commonApi.events.AccountActivatedEvent;
import app.cqrs_event_sourcing_app.commonApi.events.AccountCreatedEvent;
import app.cqrs_event_sourcing_app.commonApi.events.AccountCreditedEvent;
import app.cqrs_event_sourcing_app.commonApi.events.AccountDebitedEvent;
import app.cqrs_event_sourcing_app.exceptions.AmountNegativeException;
import app.cqrs_event_sourcing_app.exceptions.InsufficientBalanceException;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;


@Aggregate
public class AccountAggregate {
    @AggregateIdentifier
    private String accountId;
    private double balance;
    private String currency;
    private AccountStatus status;

    public AccountAggregate(){}

    @CommandHandler
    public AccountAggregate(CreateAccountCommand command) {
        if(command.getInitialBalance() < 0) throw new IllegalArgumentException("Initial balance cannot be negative");
        // OK
        AggregateLifecycle.apply(new AccountCreatedEvent(command.getId(), command.getInitialBalance(),command.getCurrency()));
    }

    @EventSourcingHandler
    public void on(AccountCreatedEvent event){
    this.accountId = event.getId();
    this.balance = event.getInitialBalance();
    this.currency = event.getCurrency();
    this.status = AccountStatus.CREATED;
    AggregateLifecycle.apply(new AccountActivatedEvent(event.getId(), AccountStatus.ACTIVATED));
    }

    @EventSourcingHandler
    public void on(AccountActivatedEvent event){
        this.accountId = event.getId();
        this.status= event.getStatus();
    }
    @CommandHandler
    public void on(CreditAccountCommand command) {
        if(command.getAmount() < 0) throw new AmountNegativeException("Amount cannot be negative");
        AggregateLifecycle.apply(new AccountCreditedEvent(command.getId(), command.getAmount(),command.getCurrency(), command.getDate()));
    }
    @EventSourcingHandler
    public void on(AccountCreditedEvent event) {
        this.balance = this.balance + event.getAmount();
    }
    // debit account
    @CommandHandler
    public void on(DebitAccountCommand command) {
        if(this.balance < command.getAmount()) throw new InsufficientBalanceException("Balance is insufficient");
        AggregateLifecycle.apply(new AccountDebitedEvent(command.getId(), command.getAmount(),command.getCurrency(), command.getDate()));
    }
    @EventSourcingHandler
    public void on(AccountDebitedEvent event) {
        this.balance = this.balance - event.getAmount();
    }


}
