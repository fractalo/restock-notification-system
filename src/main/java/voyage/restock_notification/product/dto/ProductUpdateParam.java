package voyage.restock_notification.product.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public record ProductUpdateParam(
        String name,
        @PositiveOrZero @NotNull Long stockQuantity
) { }
