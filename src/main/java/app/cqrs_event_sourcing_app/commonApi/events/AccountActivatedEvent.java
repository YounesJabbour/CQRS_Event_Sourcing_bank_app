package app.cqrs_event_sourcing_app.commonApi.events;

import app.cqrs_event_sourcing_app.commonApi.enums.AccountStatus;
import lombok.Getter;

@Getter
public class AccountActivatedEvent  extends BaseEvent<String>{
    private AccountStatus  status;
    public AccountActivatedEvent(String id, AccountStatus status) {
        super(id);
        this.status = status;
    }
}
