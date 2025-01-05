package app.cqrs_event_sourcing_app.commonApi.events;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public abstract class BaseEvent<T> {
   private T id;
}
