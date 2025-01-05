package app.cqrs_event_sourcing_app.query.repository;

import app.cqrs_event_sourcing_app.query.entities.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, String> {
}
