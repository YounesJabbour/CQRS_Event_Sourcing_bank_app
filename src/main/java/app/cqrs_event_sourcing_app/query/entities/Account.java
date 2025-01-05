package app.cqrs_event_sourcing_app.query.entities;

import app.cqrs_event_sourcing_app.commonApi.enums.AccountStatus;
import jakarta.persistence.*;
import lombok.*;

import java.util.Collection;
import java.util.List;

@AllArgsConstructor @NoArgsConstructor
@Setter
@Getter
@Builder
@Entity
public class Account {
    @Id
    private String id;
    private double solde;
    private String currency;
    @Enumerated(EnumType.STRING)
    private AccountStatus status;
    @OneToMany(mappedBy = "account")
    private Collection<Operation> operationList;
}
