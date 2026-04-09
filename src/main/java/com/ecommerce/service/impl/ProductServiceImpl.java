package com.ecommerce.service.impl;

import com.ecommerce.dto.product.ProductDTO;
import com.ecommerce.entity.Category;
import com.ecommerce.entity.Product;
import com.ecommerce.repository.CategoryRepository;
import com.ecommerce.repository.ProductRepository;
import com.ecommerce.exception.BadRequestException;
import com.ecommerce.service.ProductService;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    // TODO: Inject ProductRepository, CategoryRepository
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    @Value("${app.upload.dir:uploads}")
    private String uploadDir;

    @Override
    public ProductDTO createProduct(ProductDTO productDTO) {
        Category category = categoryRepository.findById(productDTO.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + productDTO.getCategoryId()));

        Product product = Product.builder()
                .name(productDTO.getName())
                .brand(productDTO.getBrand())
                .description(productDTO.getDescription())
                .price(productDTO.getPrice())
                .stockQuantity(productDTO.getStockQuantity())
                .imageUrl(productDTO.getImageUrl())
                .status(productDTO.getStatus())
                .category(category)
                .build();
        Product saveProduct = productRepository.save(product);
        return mapToDTO(saveProduct);
    }

    @Override
    public ProductDTO updateProduct(Long id, ProductDTO productDTO) {
        Product existedProduct = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
        Category category = categoryRepository.findById(productDTO.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + productDTO.getCategoryId()));
        existedProduct.setName(productDTO.getName());
        existedProduct.setBrand(productDTO.getBrand());
        existedProduct.setDescription(productDTO.getDescription());
        existedProduct.setPrice(productDTO.getPrice());
        existedProduct.setStockQuantity(productDTO.getStockQuantity());
        existedProduct.setImageUrl(productDTO.getImageUrl());
        existedProduct.setStatus(productDTO.getStatus());
        existedProduct.setCategory(category);
        Product updateProduct = productRepository.save(existedProduct);
        return mapToDTO(updateProduct);
    }

    @Override
    public ProductDTO getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
        return mapToDTO(product);
    }

    @Override
    public List<ProductDTO> getAllProducts() {
        return productRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .toList();
    }

    @Override
    public List<ProductDTO> getProductsByCategory(Long categoryId) {
        return productRepository.findByCategoryId(categoryId)
                .stream()
                .map(this::mapToDTO)
                .toList();
    }

    @Override
    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id" + id));
        productRepository.delete(product);
    }

    @Override
    public String uploadProductImage(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new BadRequestException("Image file is required");
        }

        String originalFilename = file.getOriginalFilename();
        String extension = StringUtils.getFilenameExtension(originalFilename);
        if (extension == null) {
            throw new BadRequestException("Invalid image file extension");
        }

        String normalizedExtension = extension.toLowerCase();
        Set<String> allowedExtensions = Set.of("jpg", "jpeg", "png", "webp", "gif");
        if (!allowedExtensions.contains(normalizedExtension)) {
            throw new BadRequestException("Only image formats jpg, jpeg, png, webp, gif are supported");
        }

        try {
            Path uploadRoot = Paths.get(uploadDir).toAbsolutePath().normalize();
            Path productUploadDir = uploadRoot.resolve("products").normalize();

            if (!productUploadDir.startsWith(uploadRoot)) {
                throw new BadRequestException("Invalid upload directory");
            }

            Files.createDirectories(productUploadDir);

            String safeFilename = UUID.randomUUID() + "." + normalizedExtension;
            Path targetFile = productUploadDir.resolve(safeFilename).normalize();
            Files.copy(file.getInputStream(), targetFile, StandardCopyOption.REPLACE_EXISTING);

            return "/uploads/products/" + safeFilename;
        } catch (IOException ex) {
            throw new RuntimeException("Cannot upload product image", ex);
        }
    }

    private ProductDTO mapToDTO(Product product) {
        return ProductDTO.builder()
                .id(product.getId())
                .name(product.getName())
                .brand(product.getBrand())
                .description(product.getDescription())
                .price(product.getPrice())
                .stockQuantity(product.getStockQuantity())
                .imageUrl(product.getImageUrl())
                .status(product.getStatus())
                .categoryId(
                        product.getCategory() != null ? product.getCategory().getId() : null)
                .categoryName(
                        product.getCategory() != null ? product.getCategory().getName() : null)
                .build();
    }
}

