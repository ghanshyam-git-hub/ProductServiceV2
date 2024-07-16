package dev.ghanshyam.productservicev2.repositories;

import dev.ghanshyam.productservicev2.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface CategoryServiceRepo extends JpaRepository<Category, UUID> {

    @Query("select c from Category c")
    List<Category> getAllCategories();

    @Query("select c from Category c where c.category LIKE %:searchstring%")
    List<Category> getCategoriesLike(String searchstring);

    @Query("select c from Category c where c.category=:cat_name")
    Category getCategoryObjectByName(String cat_name);

    Category save(Category c);
}
