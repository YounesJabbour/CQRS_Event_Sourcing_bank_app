package app.cqrs_event_sourcing_app.commonApi.commands;

import lombok.Getter;

import java.util.Date;

@Getter
public class CreditAccountCommand extends BaseCommand<String> {
    private double amount;
    private String currency;
    private Date date;

    public CreditAccountCommand(String id, double amount, String currency, Date date) {
        super(id);
        this.amount = amount;
        this.currency = currency;
        this.date = date;
    }
}
