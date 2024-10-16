package voyage.restock_notification.product.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import voyage.restock_notification.product.dto.ProductUpdateParam;
import voyage.restock_notification.product.dto.SimpleProductInfo;
import voyage.restock_notification.product.service.ProductService;

@RestController
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @PutMapping("/products/{productId}")
    public SimpleProductInfo updateProduct(@PathVariable("productId") Long productId,
                                           @Valid @RequestBody ProductUpdateParam updateParam) {
        return productService.updateProduct(productId, updateParam);
    }
}
