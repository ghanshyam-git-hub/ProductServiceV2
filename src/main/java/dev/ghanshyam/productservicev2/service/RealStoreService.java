package dev.ghanshyam.productservicev2.service;

import dev.ghanshyam.productservicev2.dtos.AddProductDto;
import dev.ghanshyam.productservicev2.dtos.ProductDto;
import dev.ghanshyam.productservicev2.dtos.UpdateProductDto;
import dev.ghanshyam.productservicev2.exception.AddException;
import dev.ghanshyam.productservicev2.exception.DeleteException;
import dev.ghanshyam.productservicev2.exception.NotFoundException;
import dev.ghanshyam.productservicev2.models.Category;
import dev.ghanshyam.productservicev2.models.Product;
import dev.ghanshyam.productservicev2.repositories.CategoryServiceRepo;
import dev.ghanshyam.productservicev2.repositories.ProductServiceRepo;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Qualifier("RealStoreProductService")
public class RealStoreService implements ProductsService{
    ProductServiceRepo productServiceRepo;
    CategoryServiceRepo categoryServiceRepo;

    public RealStoreService(ProductServiceRepo productServiceRepo,CategoryServiceRepo categoryServiceRepo){
        this.productServiceRepo = productServiceRepo;
        this.categoryServiceRepo = categoryServiceRepo;
    }

    @Override
    public ProductDto getProductsById(Long id) throws NotFoundException {
        Product product = productServiceRepo.getProductByProduct_id(id);
        if(product==null) throw new NotFoundException("Requested Product does not exists");
        return convertProductToProductDto(product);
    }

    @Override
    public List<ProductDto> getAllProducts() throws NotFoundException {
        List<Product> productList = productServiceRepo.getAllProducts();
        if(productList.size()==0) throw new NotFoundException("There is no Entry yet!");
        List<ProductDto> productDtoList = productList.stream().map((product)->convertProductToProductDto(product)).toList();
        return productDtoList;
    }

    @Override
    public List<String> getAllCategories() throws NotFoundException {
        List<Category> categoryList = categoryServiceRepo.getAllCategories();
        if(categoryList.size()==0)throw new NotFoundException("No Categories added yet");
        return categoryList.stream().map((category)->category.getCategory()).toList();
    }

    @Override
    public List<ProductDto> getProductsInCategory(String category) throws NotFoundException {
        List<Product> productList = productServiceRepo.getProductsByCategory_Category(category);
        if(productList.size()==0) throw new NotFoundException("No Products in this Category");
        List<ProductDto> productDtoList = productList.stream().map((product)->convertProductToProductDto(product)).toList();
        return productDtoList;
    }

    public List<ProductDto> getProductsInCategoriesLike(String searchstring) throws NotFoundException {
        List<Category> categoryList = categoryServiceRepo.getCategoriesLike(searchstring);
        if(categoryList.size()==0) throw new NotFoundException("No such Category");
        List<Product> productList = productServiceRepo.getAllByCategoryIn(categoryList);
        List<ProductDto> productDtoList = productList.stream().map((product)->convertProductToProductDto(product)).toList();
        return productDtoList;
    }

    /*
    Searching and sorting products
     */
    @Override
    public Page<ProductDto> searchby(Integer page_no, Integer pagesize, String sortdirection, String sortby) {
        Sort sort = null;
        if(sortdirection.equalsIgnoreCase("asc"))
         sort = Sort.by(Sort.Direction.ASC,sortby);
        else
         sort = Sort.by(Sort.Direction.DESC,sortby);

        Pageable pageable = PageRequest.of(page_no,pagesize,sort);
        Page<Product> productPage = productServiceRepo.findAllBy(pageable);
        Page<ProductDto>productDtoPage = productPage.map((p)-> convertProductToProductDto(p)); // Product should not be send in th o/p else infinite nesting
        return productDtoPage;
    }

    @Override
    public ProductDto addProduct(AddProductDto addProductDto) throws AddException {
        Product product = new Product();
        Long lastProductId = productServiceRepo.getLastProductId();
        Category category = null;
        if(lastProductId==null){ // no product yet, ie no category also yet
            lastProductId = 0l;
            List<Product>productList = new ArrayList<>();
            productList.add(product);
            product.setCategory(new Category(addProductDto.getCategory() ,productList));
        }else{ // product id is not null but check if the category exist
             category = categoryServiceRepo.getCategoryObjectByName(addProductDto.getCategory());
            if(category==null){ // we need to add this category and then assign it to this product
                List<Product>productList = new ArrayList<>();
                productList.add(product);
                product.setCategory(categoryServiceRepo.save(new Category(addProductDto.getCategory(),productList)));
            }else{
                product.setCategory(category);
            }
        }
        product.setProductId(lastProductId+1);
        product.setTitle(addProductDto.getTitle());
        product.setPrice(addProductDto.getPrice());
        product.setDescription(addProductDto.getDescription());
        product.setImageurl(addProductDto.getImage());

        Product savedProduct = productServiceRepo.save(product);
        return convertProductToProductDto(savedProduct);
    }

    @Override
    public ProductDto updateProduct(Long id, UpdateProductDto updateProductDto) throws NotFoundException {
        Product product = productServiceRepo.getProductByProduct_id(id);
        if(product==null) throw new NotFoundException("This Product id does not exist");

        product.setTitle(updateProductDto.getTitle());
        product.setPrice(updateProductDto.getPrice());

        if(product.getCategory()==null)
            product.setCategory(categoryServiceRepo.getCategoryObjectByName(updateProductDto.getCategory()));

        product.setDescription(updateProductDto.getDescription());
        product.setImageurl(updateProductDto.getImage());

        Product savedProduct = productServiceRepo.save(product);
        return convertProductToProductDto(savedProduct);
    }

    @Override
    public ProductDto deleteProduct(Long id) throws DeleteException {
        Product product = productServiceRepo.getProductByProduct_id(id);
        if(product==null) throw new DeleteException("Object with this id does not exist!");
        productServiceRepo.delete(product);
        return convertProductToProductDto(product);
    }

    public ProductDto convertProductToProductDto(Product product){
        ProductDto productDto = new ProductDto();
        productDto.setId(product.getProductId());
        productDto.setCategory(product.getCategory().getCategory());
        productDto.setTitle(product.getTitle());
        productDto.setPrice(product.getPrice());
        productDto.setImage(product.getImageurl());
        productDto.setDescription(product.getDescription());
        return productDto;
    }
}
