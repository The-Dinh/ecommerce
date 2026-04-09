package com.ecommerce.service;

import com.ecommerce.dto.product.ProductDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductService {

    ProductDTO createProduct(ProductDTO productDTO);

    ProductDTO updateProduct(Long id, ProductDTO productDTO);

    ProductDTO getProductById(Long id);

    List<ProductDTO> getAllProducts();

    List<ProductDTO> getProductsByCategory(Long categoryId);

    void deleteProduct(Long id);

    String uploadProductImage(MultipartFile file);
}

