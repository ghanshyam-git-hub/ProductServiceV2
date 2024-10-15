package dev.ghanshyam.productservicev2.controllers;

import dev.ghanshyam.productservicev2.dtos.AddProductDto;
import dev.ghanshyam.productservicev2.dtos.ProductDto;
import dev.ghanshyam.productservicev2.dtos.UpdateProductDto;
import dev.ghanshyam.productservicev2.exception.AddException;
import dev.ghanshyam.productservicev2.exception.DeleteException;
import dev.ghanshyam.productservicev2.exception.NotFoundException;
import dev.ghanshyam.productservicev2.service.ProductsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@RestController
@RequestMapping("/products")
public class ProductController {
    ProductsService productsService;
    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);
//    ProductController(@Qualifier("FakeStoreProductService") ProductsService productsService){
//        this.productsService = productsService;
//    }


    ProductController(@Qualifier("RealStoreProductService") ProductsService productsService){
        this.productsService = productsService;
    }
    /*
    1 TRACE logs a very fine-grained message,
    2 DEBUG logs helpful debugging information,
    3 INFO logs a general event message,
    4 WARN logs a warning, and
    5 ERROR logs a serious error.

    TRACE is most detailed and error is least, so if package logging is trace then all below will be logged
    But if logging of the package is INFO - then Logging level - INFO,WARN and ERROR will be displayed (if they are there in the code) , other log even if there will not appear on the screen
    Default log level of the project is INFO, so if we want to use logs of debug level then we should first change the log level in the properties file

    So in development we can use detailed logs but in production we can only display INFO logs through prperties file log setting
     */

    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getProductById(@PathVariable ("id") Long id) throws NotFoundException {
        logger.debug("Get mapping on /id - entered getProductById method"); // this will be only displayed if this package logging.level.dev.ghanshyam.productservicev2=DEBUG or TRACE, if not then this will not be displayed
        ProductDto productDto = productsService.getProductsById(id);
        ResponseEntity<ProductDto>responseEntity = new ResponseEntity<>(productDto, HttpStatus.OK);
        logger.debug("Get mapping on /id - exited getProductById method");
        return responseEntity;
    }

/*
Both of the following methods will work the same way:

Without value:

java
Copy code
@GetMapping("/users")
public ResponseEntity<List<UserDTO>> getUsers(
        @RequestParam(required = false) String name,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size,
        @RequestParam(required = false) String sort) {
    // Logic
}
With value:

java
Copy code
@GetMapping("/users")
public ResponseEntity<List<UserDTO>> getUsers(
        @RequestParam(value = "name", required = false) String userName,
        @RequestParam(value = "page", defaultValue = "0") int page,
        @RequestParam(value = "size", defaultValue = "10") int size,
        @RequestParam(value = "sort", required = false) String sort) {
    // Logic
}
Conclusion
Using value is optional. The choice depends on your coding style or if you want to clarify the expected query parameter name. If you're using method parameter names that are clear and match the expected query parameters, omitting value can keep the code concise.

 */
    @GetMapping("/search")
    public ResponseEntity<Page<ProductDto>> searchby(
            @RequestParam(value = "page_no",defaultValue = "1") Integer page_no,
            @RequestParam(value = "page_size",defaultValue = "1") Integer pagesize ,
            @RequestParam(value = "sort_dir",defaultValue = "asc") String sortdirection,
            @RequestParam(value = "sort_by",defaultValue = "productId") String sortby
            )
    {
        return  new ResponseEntity<>(productsService.searchby(page_no,pagesize,sortdirection,sortby),HttpStatus.OK);
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

    /*
    /category/search?cat_name="xyz"
     */
    @GetMapping("/category/search")
    public ResponseEntity<List<ProductDto>> getProductsInCategoriesLike(@RequestParam("cat_name") String category) throws NotFoundException {
        List<ProductDto>productDtoList = productsService.getProductsInCategoriesLike(category);
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
