package com.ecommerce.service;

import com.ecommerce.dto.product.ProductDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductService {

    ProductDTO createProduct(ProductDTO productDTO);

    ProductDTO updateProduct(Long id, ProductDTO productDTO);

    ProductDTO getProductById(Long id);

    List<ProductDTO> getAllProducts();

    Page<ProductDTO> getPaginatedProducts(String keyword, Pageable pageable);

    Page<ProductDTO> getProductsByCategory(Long categoryId, Pageable pageable);

    List<ProductDTO> getProductsByCategory(Long categoryId);

    void deleteProduct(Long id);

    String uploadProductImage(MultipartFile file);
}

