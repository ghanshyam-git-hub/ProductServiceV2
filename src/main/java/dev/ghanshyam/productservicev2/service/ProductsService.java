package dev.ghanshyam.productservicev2.service;

import dev.ghanshyam.productservicev2.dtos.AddProductDto;
import dev.ghanshyam.productservicev2.dtos.ProductDto;
import dev.ghanshyam.productservicev2.dtos.UpdateProductDto;
import dev.ghanshyam.productservicev2.exception.AddException;
import dev.ghanshyam.productservicev2.exception.DeleteException;
import dev.ghanshyam.productservicev2.exception.NotFoundException;
import dev.ghanshyam.productservicev2.models.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductsService {
    public ProductDto getProductsById(Long id) throws NotFoundException;

    public List<ProductDto> getAllProducts() throws NotFoundException;

    public List<String> getAllCategories() throws NotFoundException;

    public List<ProductDto> getProductsInCategory(String category) throws NotFoundException;

    public ProductDto addProduct(AddProductDto addProductDto) throws AddException;

    public ProductDto updateProduct(Long id, UpdateProductDto updateProductDto) throws NotFoundException;

    public ProductDto deleteProduct(Long id) throws DeleteException;

    public List<ProductDto> getProductsInCategoriesLike(String searchstring) throws NotFoundException;

    public Page<ProductDto> searchby(Integer page_no, Integer pagesize, String sortdirection, String sortby);

}
