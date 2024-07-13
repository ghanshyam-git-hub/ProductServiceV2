package dev.ghanshyam.productservicev2.service;

import dev.ghanshyam.productservicev2.dtos.AddProductDto;
import dev.ghanshyam.productservicev2.dtos.ProductDto;
import dev.ghanshyam.productservicev2.dtos.UpdateProductDto;
import dev.ghanshyam.productservicev2.exception.AddException;
import dev.ghanshyam.productservicev2.exception.DeleteException;
import dev.ghanshyam.productservicev2.exception.NotFoundException;
import dev.ghanshyam.productservicev2.models.Category;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.MissingFormatArgumentException;

@Service
@Qualifier("FakeStoreProductService")
public class FakeStoreService implements ProductsService{

    RestTemplateBuilder restTemplateBuilder;
    @Value("${fakestore_api}")
    String fakestoreapi;

    @Value("${products_path}")
    String products_path;

    @Value("${categories_path}")
    String categories_path;

    @Value("${category_path}")
    String category_path;

    public FakeStoreService(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplateBuilder = new RestTemplateBuilder();
    }

    @Override
    public ProductDto getProductsById(Long id) throws NotFoundException {
        RestTemplate restTemplate = restTemplateBuilder.build();
        ProductDto productDto = restTemplate.getForObject(fakestoreapi+products_path+"/"+id , ProductDto.class);

        if(productDto==null) throw new NotFoundException("Requested Product does not exists");

        return productDto;
    }

    @Override
    public List<ProductDto> getAllProducts() throws NotFoundException {
        RestTemplate restTemplate = restTemplateBuilder.build();
        ProductDto[]productDtos = restTemplate.getForObject(fakestoreapi+products_path,ProductDto[].class);
        List<ProductDto> listProductDtos = Arrays.asList(productDtos);
        if(listProductDtos.size()==0) throw new NotFoundException("No Product available yet, will add soon!");
        return Arrays.asList(productDtos);
    }

    @Override
    public List<String> getAllCategories() throws NotFoundException {
        RestTemplate restTemplate = restTemplateBuilder.build();
        String[]categoryStringArray = restTemplate.getForObject(fakestoreapi+products_path+categories_path,String[].class);
        List<String> categoryList = Arrays.asList(categoryStringArray);
        if(categoryList.size()==0) throw new NotFoundException("No products in any category so far");

        return categoryList;
    }

    @Override
    public List<ProductDto> getProductsInCategory(String category) throws NotFoundException {
        RestTemplate restTemplate = restTemplateBuilder.build();
        ProductDto[]productDtos =  restTemplate.getForObject(fakestoreapi+products_path+category_path+"/jewelery",ProductDto[].class);
        List<ProductDto> productDtoList = Arrays.asList(productDtos);
        if(productDtoList.size()==0) throw new NotFoundException("No Products in this category yet");
        return productDtoList;
    }

    @Override
    public ProductDto addProduct(AddProductDto addProductDto) throws AddException {
        RestTemplate restTemplate = restTemplateBuilder.build();
        ProductDto productDto = restTemplate.postForObject(fakestoreapi+products_path,addProductDto,ProductDto.class);

        if(productDto==null) throw new AddException("Could not add this product, pls try again");
        return productDto;
    }

    @Override
    public ProductDto updateProduct(Long id, UpdateProductDto updateProductDto) {
        RestTemplate restTemplate = restTemplateBuilder.build();
        restTemplate.put(fakestoreapi+products_path+"/"+id,updateProductDto);
        ProductDto productDto = restTemplate.getForObject(fakestoreapi+products_path+"/"+id,ProductDto.class);
        return productDto;
    }

    @Override
    public ProductDto deleteProduct(Long id) throws DeleteException {
        RestTemplate restTemplate = restTemplateBuilder.build();
        ProductDto deletedProduct = restTemplate.getForObject(fakestoreapi+products_path+"/"+id,ProductDto.class);
        restTemplate.delete(fakestoreapi+products_path+"/"+id);
        if(deletedProduct==null) throw new DeleteException("This product does not exist, so cant be deleted");
        return deletedProduct;
    }
}
