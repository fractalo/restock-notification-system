package voyage.restock_notification.product.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import voyage.restock_notification.product.dto.ProductUpdateParam;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long id;

    private String name;

    @PositiveOrZero
    @NotNull
    private Long stockQuantity = 0L;

    private Long restockRound = 0L;

    public void update(ProductUpdateParam param) {
        this.name = param.name();
        this.stockQuantity = param.stockQuantity();
    }

    public void increaseRestockRound() {
        ++this.restockRound;
    }

}
