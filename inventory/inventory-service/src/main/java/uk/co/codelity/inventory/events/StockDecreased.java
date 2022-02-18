package uk.co.codelity.inventory.events;

import uk.co.codelity.event.sourcing.common.annotation.Event;

@Event(name="inventory-service.stock-decreased")
public class StockDecreased {
}
