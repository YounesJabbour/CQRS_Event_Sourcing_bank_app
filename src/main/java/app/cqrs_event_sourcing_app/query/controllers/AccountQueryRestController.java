package app.cqrs_event_sourcing_app.query.controllers;


import app.cqrs_event_sourcing_app.commonApi.queries.GetAccountByIdQuery;
import app.cqrs_event_sourcing_app.commonApi.queries.GetAllAccountsQuery;
import app.cqrs_event_sourcing_app.query.entities.Account;
import lombok.AllArgsConstructor;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "/query/accounts")
@AllArgsConstructor
public class AccountQueryRestController {
    private QueryGateway queryGateway;


    @GetMapping(path = "/accounts")
    public List<Account> getAll(){
        return queryGateway.query(new GetAllAccountsQuery(),
                ResponseTypes.multipleInstancesOf(Account.class)).join();
    }

    @GetMapping("/{accountId}")
    public Account getAccountById(@PathVariable String accountId){
        return queryGateway.query(new GetAccountByIdQuery(accountId),
                ResponseTypes.instanceOf(Account.class)).join();
    }
}
