package app.cqrs_event_sourcing_app.commonApi.dtos;

import lombok.Getter;

@Getter
public class debitAccountRequestDTO {
    private String accountId;
    private double amount;
    private String currency;
}
