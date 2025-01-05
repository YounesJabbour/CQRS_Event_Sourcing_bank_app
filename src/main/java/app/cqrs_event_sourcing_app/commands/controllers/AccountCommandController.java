package app.cqrs_event_sourcing_app.commands.controllers;


import app.cqrs_event_sourcing_app.commonApi.commands.CreateAccountCommand;
import app.cqrs_event_sourcing_app.commonApi.commands.CreditAccountCommand;
import app.cqrs_event_sourcing_app.commonApi.commands.DebitAccountCommand;
import app.cqrs_event_sourcing_app.commonApi.dtos.CreateAccountRequestDTO;
import app.cqrs_event_sourcing_app.commonApi.dtos.CreditAccountRequestDTO;
import app.cqrs_event_sourcing_app.commonApi.dtos.debitAccountRequestDTO;
import lombok.AllArgsConstructor;
import org.apache.coyote.Response;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.eventsourcing.eventstore.EventStore;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

@RestController
@RequestMapping("/commands/account")
@AllArgsConstructor
public class AccountCommandController {
    private CommandGateway commandGateway;
    private EventStore eventStore;

    @PostMapping("/create")
    public CompletableFuture<ResponseEntity<String>> createAccount(@RequestBody CreateAccountRequestDTO req) {
        return commandGateway.send(new CreateAccountCommand(
                UUID.randomUUID().toString(),
                req.getCurrency(),
                req.getInitialBalance()
        ))
                .thenApply(result -> ResponseEntity.ok("Account created successfully"+result))
                .exceptionally(ex -> {
                    return new ResponseEntity<>("Error creating account: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
                });
    }

    @PutMapping("/debit")
    public CompletableFuture<String> debitAccount(@RequestBody debitAccountRequestDTO req) {
        return commandGateway.send(new DebitAccountCommand(req.getAccountId(), req.getAmount(), req.getCurrency(), new Date()));
    }

    @PutMapping("/credit")
    public CompletableFuture<String> creditAccount(@RequestBody CreditAccountRequestDTO req) {
        return commandGateway.send(new CreditAccountCommand(req.getAccountId(), req.getAmount(), req.getCurrency(), new Date()));
    }

    @GetMapping("/eventStore/{accountId}")
    public Stream eventStore(@PathVariable String accountId) {
        return eventStore.readEvents(accountId).asStream();
    }
}
