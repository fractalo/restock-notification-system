package voyage.restock_notification.product.dto;

import voyage.restock_notification.product.domain.Product;

public record SimpleProductInfo(
        Long id,
        String name,
        Long stockQuantity
) {
    public SimpleProductInfo(Product product) {
        this(product.getId(), product.getName(), product.getStockQuantity());
    }
}
