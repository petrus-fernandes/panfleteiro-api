package br.com.promo.panfleteiro.service;

import br.com.promo.panfleteiro.entity.Ad;
import br.com.promo.panfleteiro.entity.Product;
import br.com.promo.panfleteiro.entity.ProductCategory;
import br.com.promo.panfleteiro.exception.ResourceNotFoundException;
import br.com.promo.panfleteiro.repository.ProductRepository;
import br.com.promo.panfleteiro.request.ProductRequest;
import br.com.promo.panfleteiro.response.AdResponse;
import br.com.promo.panfleteiro.response.ProductResponse;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> findAll() {
        return productRepository.findAll();
    }

    public Product findById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + id));
    }

    public ProductResponse create(ProductRequest productRequest) {
        Product product = new Product();
        product.setName(productRequest.getName());
        product.setProductCategory(ProductCategory.fromCode(productRequest.getProductCategory()));
        return convertToProductResponse(productRepository.save(product));
    }

    public ProductResponse update(Long id, ProductRequest productRequest) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(id.toString()));
        product.setName(productRequest.getName());
        product.setProductCategory(ProductCategory.fromCode(productRequest.getProductCategory()));
        return convertToProductResponse(productRepository.save(product));
    }

    public void delete(Long id) {
        if (productRepository.existsById(id)) {
            productRepository.deleteById(id);
        } else {
            throw new ResourceNotFoundException(id.toString());
        }
    }

    public ProductResponse convertToProductResponse(Product product) {
        List<Long> adsId = product.getAds().stream().map(Ad::getId).toList();
        return new ProductResponse(product.getId(), product.getName(), product.getProductCategory().getCode(), adsId);
    }

}