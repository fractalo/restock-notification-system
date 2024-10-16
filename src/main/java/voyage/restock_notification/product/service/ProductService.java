package voyage.restock_notification.product.service;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import voyage.restock_notification.product.domain.Product;
import voyage.restock_notification.product.dto.ProductUpdateParam;
import voyage.restock_notification.product.dto.SimpleProductInfo;
import voyage.restock_notification.product.event.RestockEvent;
import voyage.restock_notification.product.repository.ProductRepository;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public SimpleProductInfo updateProduct(Long productId, ProductUpdateParam updateParam) {
        Product product = productRepository.findByIdWithLock(productId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 상품입니다."));

        if (product.getStockQuantity() == 0 && updateParam.stockQuantity() > 0) {
            product.increaseRestockRound();
            eventPublisher.publishEvent(new RestockEvent(product));
        }

        product.update(updateParam);
        return new SimpleProductInfo(product);
    }

}
