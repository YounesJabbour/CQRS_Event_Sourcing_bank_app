package app.cqrs_event_sourcing_app.commonApi.dtos;


import lombok.*;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CreateAccountRequestDTO {
    private double initialBalance;
    private String currency;
}
