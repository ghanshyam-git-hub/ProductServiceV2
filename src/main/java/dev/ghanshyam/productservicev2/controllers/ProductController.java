package dev.ghanshyam.productservicev2.controllers;

import dev.ghanshyam.productservicev2.dtos.AddProductDto;
import dev.ghanshyam.productservicev2.dtos.ProductDto;
import dev.ghanshyam.productservicev2.dtos.UpdateProductDto;
import dev.ghanshyam.productservicev2.exception.AddException;
import dev.ghanshyam.productservicev2.exception.DeleteException;
import dev.ghanshyam.productservicev2.exception.ExceptionDto;
import dev.ghanshyam.productservicev2.exception.NotFoundException;
import dev.ghanshyam.productservicev2.models.Category;
import dev.ghanshyam.productservicev2.service.ProductsService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.MissingFormatArgumentException;

@RestController
@RequestMapping("/products")
public class ProductController {
    ProductsService productsService;

    /*
    ProductController(@Qualifier("FakeStoreProductService") ProductsService productsService){
        this.productsService = productsService;
    }
    */
    ProductController(@Qualifier("RealStoreProductService") ProductsService productsService){
        this.productsService = productsService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getProductById(@PathVariable ("id") Long id) throws NotFoundException {

        ProductDto productDto = productsService.getProductsById(id);
        ResponseEntity<ProductDto>responseEntity = new ResponseEntity<>(productDto, HttpStatus.OK);
        return responseEntity;
    }

    @GetMapping
    public ResponseEntity<List<ProductDto>> getAllProducts() throws NotFoundException {
        List<ProductDto> list = productsService.getAllProducts();
        ResponseEntity<List<ProductDto>> responseEntity = new ResponseEntity<>(list,HttpStatus.OK);
        return responseEntity;
    }

    @GetMapping("/categories")
    public ResponseEntity<List<String>> getAllCategories() throws NotFoundException {
        List<String> categoryList = productsService.getAllCategories();
        ResponseEntity<List<String>> responseEntity = new ResponseEntity<>(categoryList,HttpStatus.OK);
        return responseEntity;
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<List<ProductDto>> getProductsInCategory(@PathVariable("category") String category) throws NotFoundException {
        List<ProductDto>productDtoList = productsService.getProductsInCategory(category);
        ResponseEntity<List<ProductDto>> responseEntity = new ResponseEntity<>(productDtoList,HttpStatus.OK);
        return responseEntity;
    }

    @PostMapping
    public ResponseEntity<ProductDto> addProduct(@RequestBody AddProductDto addProductDto) throws AddException{
        ProductDto addedProductDto = productsService.addProduct(addProductDto);
        ResponseEntity<ProductDto> responseEntity = new ResponseEntity<>(addedProductDto,HttpStatus.OK);
        return responseEntity;
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDto> updateProduct(@RequestBody UpdateProductDto updateProductDto,@PathVariable("id")Long id) throws NotFoundException {
        ProductDto productDto = productsService.updateProduct(id,updateProductDto);
        ResponseEntity<ProductDto>responseEntity = new ResponseEntity<>(productDto,HttpStatus.OK);
        return responseEntity;
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ProductDto> partialUpdateProduct(@RequestBody UpdateProductDto updateProductDto,@PathVariable("id")Long id) throws NotFoundException {
        ProductDto productDto = productsService.updateProduct(id,updateProductDto);
        ResponseEntity<ProductDto>responseEntity = new ResponseEntity<>(productDto,HttpStatus.OK);
        return responseEntity;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ProductDto> deleteProduct(@PathVariable("id") Long id) throws DeleteException {
        ProductDto productDto = productsService.deleteProduct(id);
        ResponseEntity<ProductDto>responseEntity = new ResponseEntity<>(productDto,HttpStatus.OK);
        return responseEntity;
    }

}
