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

    private ProductRepository productRepository;

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
        Product product = productRepository.save(convertToProduct(productRequest));
        return convertToProductResponse(product);
    }

    private Product convertToProduct(ProductRequest productRequest) {
//        List<Ad> ads = productRequest.getAdIds().isEmpty() ? new ArrayList<>() : productRequest.getAdIds().stream().map(adService::findById).toList();
        return new Product(productRequest.getName(), ProductCategory.fromCode(productRequest.getProductCategory()));
    }

    public ProductResponse update(Long id, ProductRequest product) {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(id.toString()));
        existingProduct.setName(product.getName());
        existingProduct.setProductCategory(ProductCategory.fromCode(product.getProductCategory()));
//        existingProduct.setAds(product.getAdIds().stream().map(adService::findById).toList());
        existingProduct = productRepository.save(existingProduct);
        return convertToProductResponse(existingProduct);
    }

    public void delete(Long id) {
        if (productRepository.existsById(id)) {
            productRepository.deleteById(id);
        } else {
            throw new ResourceNotFoundException(id.toString());
        }
    }

    public ProductResponse convertToProductResponse(Product product) {
//        List<AdResponse> adsResponse = product.getAds().stream().map(adService::convertToAdResponse).toList();
        return new ProductResponse(product.getId(), product.getName(), product.getProductCategory().getCode());
    }

    public ProductResponse getProductResponseById(Long id) {
        return convertToProductResponse(findById(id));
    }

}