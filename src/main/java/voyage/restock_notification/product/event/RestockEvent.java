package voyage.restock_notification.product.event;

import voyage.restock_notification.product.domain.Product;

public class RestockEvent {
    Long productId;
    Long restockRound;

    public RestockEvent(Product product) {
        this.productId = product.getId();
        this.restockRound = product.getRestockRound();
    }
}
