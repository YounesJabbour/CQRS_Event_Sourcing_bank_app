package app.cqrs_event_sourcing_app.commonApi.commands;

import lombok.Getter;
import lombok.Setter;
import org.axonframework.modelling.command.TargetAggregateIdentifier;


@Getter
public class CreateAccountCommand extends BaseCommand<String> {
    private double  initialBalance;
    private String currency;

    public CreateAccountCommand(String id, String currency, double initialBalance){
        super(id);
        this.initialBalance = initialBalance;
        this.currency = currency;
    }
}
