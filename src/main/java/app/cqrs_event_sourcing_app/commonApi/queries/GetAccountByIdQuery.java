package app.cqrs_event_sourcing_app.commonApi.queries;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor @NoArgsConstructor @Data
public class GetAccountByIdQuery {
    private String id;
}
