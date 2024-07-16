package dev.ghanshyam.productservicev2.repositories;

import dev.ghanshyam.productservicev2.models.Category;
import dev.ghanshyam.productservicev2.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ProductServiceRepo extends JpaRepository<Product, UUID> {

    @Query("select p from Product p where p.product_id=:id")
    public Product getProductByProduct_id(Long id);

    @Query("select p from Product p")
    public List<Product> getAllProducts();

    @Query("select MAX(p.product_id) from Product p")
    public Long getLastProductId();

    @Query("select p from Product p JOIN Category c ON p.category=c where c.category=:cat_name")
    public List<Product> getProductsByCategory_Category(String cat_name);

    public List<Product>getAllByCategoryIn(List<Category>categoryList);

    public Product save(Product p);

    public void delete(Product product);
}
