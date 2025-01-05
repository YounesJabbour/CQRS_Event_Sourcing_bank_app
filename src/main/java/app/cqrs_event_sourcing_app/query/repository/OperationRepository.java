package app.cqrs_event_sourcing_app.query.repository;

import app.cqrs_event_sourcing_app.query.entities.Account;
import app.cqrs_event_sourcing_app.query.entities.Operation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OperationRepository extends JpaRepository<Operation, String> {
}
