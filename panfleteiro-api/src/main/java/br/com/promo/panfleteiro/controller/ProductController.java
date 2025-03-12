package br.com.promo.panfleteiro.controller;

import br.com.promo.panfleteiro.request.ProductRequest;
import br.com.promo.panfleteiro.response.ProductResponse;
import br.com.promo.panfleteiro.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/produtos")
public class ProductController {

    private ProductService productService;

    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<List<ProductResponse>> findAll() {
        logger.info("Listing all Products");
        return ResponseEntity.ok(productService.findAll().stream().map(productService::convertToProductResponse).toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> findById(@PathVariable Long id) {
        logger.info("Looking for Product with ID: {}", id);
        return ResponseEntity.ok(productService.convertToProductResponse(productService.findById(id)));

    }

    @PostMapping
    public ResponseEntity<ProductResponse> create(@RequestBody ProductRequest productRequest) {
        logger.info("Creating Product: {}", productRequest);
        ProductResponse productResponse = productService.create(productRequest);
        logger.info("Created Product: {}", productResponse.getId());
        return ResponseEntity.status(201).body(productResponse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> update(@PathVariable Long id, @RequestBody ProductRequest product) {
        logger.info("Updating Product with ID: {}", id);
        return ResponseEntity.ok(productService.update(id, product));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        logger.info("Deleting Product with ID: {}", id);
        productService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
