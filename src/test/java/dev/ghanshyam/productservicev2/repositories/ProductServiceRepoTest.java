package dev.ghanshyam.productservicev2.repositories;

import dev.ghanshyam.productservicev2.models.Category;
import dev.ghanshyam.productservicev2.models.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ProductServiceRepoTest {

    @Mock
    private ProductServiceRepo productServiceRepo;

    private Category category;
    private Product product;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        category = new Category();
        category.setCategory("Category_1");

        product = Product.builder()
                .productId(1L)
                .title("Product_1")
                .description("First Product")
                .imageurl("http://example.com/image.png")
                .price(100.0)
                .category(category)
                .build();
    }

    @Test
    void saveProductTest() {
        when(productServiceRepo.save(any(Product.class))).thenReturn(product);

        Product savedProduct = productServiceRepo.save(product);

        assertNotNull(savedProduct);
        assertEquals(product.getTitle(), savedProduct.getTitle());
        verify(productServiceRepo, times(1)).save(product);
    }

    @Test
    void getProductByProductIdTest() {
        when(productServiceRepo.getProductByProduct_id(1L)).thenReturn(product);

        Product foundProduct = productServiceRepo.getProductByProduct_id(1L);

        assertNotNull(foundProduct);
        assertEquals(product.getTitle(), foundProduct.getTitle());
        verify(productServiceRepo, times(1)).getProductByProduct_id(1L);
    }

    @Test
    void getAllProductsTest() {
        List<Product> productList = new ArrayList<>(Collections.singletonList(product));
        when(productServiceRepo.getAllProducts()).thenReturn(productList);

        List<Product> allProducts = productServiceRepo.getAllProducts();

        assertEquals(1, allProducts.size());
        assertEquals(product.getTitle(), allProducts.get(0).getTitle());
        verify(productServiceRepo, times(1)).getAllProducts();
    }

    @Test
    void getLastProductIdTest() {
        when(productServiceRepo.getLastProductId()).thenReturn(1L);

        Long lastProductId = productServiceRepo.getLastProductId();

        assertEquals(1L, lastProductId);
        verify(productServiceRepo, times(1)).getLastProductId();
    }

    @Test
    void getProductsByCategoryTest() {
        when(productServiceRepo.getProductsByCategory_Category(category.getCategory())).thenReturn(Collections.singletonList(product));

        List<Product> productsByCategory = productServiceRepo.getProductsByCategory_Category(category.getCategory());

        assertEquals(1, productsByCategory.size());
        assertEquals(product.getTitle(), productsByCategory.get(0).getTitle());
        verify(productServiceRepo, times(1)).getProductsByCategory_Category(category.getCategory());
    }

    @Test
    void getAllByCategoryInTest() {
        List<Category> categories = Collections.singletonList(category);
        when(productServiceRepo.getAllByCategoryIn(categories)).thenReturn(Collections.singletonList(product));

        List<Product> products = productServiceRepo.getAllByCategoryIn(categories);

        assertEquals(1, products.size());
        assertEquals(product.getTitle(), products.get(0).getTitle());
        verify(productServiceRepo, times(1)).getAllByCategoryIn(categories);
    }
}
