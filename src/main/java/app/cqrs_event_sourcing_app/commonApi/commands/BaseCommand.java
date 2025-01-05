package app.cqrs_event_sourcing_app.commonApi.commands;

import lombok.Getter;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

public class BaseCommand<T> {
    @TargetAggregateIdentifier
    @Getter
    private T id;

 public BaseCommand(T id) { this.id = id;}
}
