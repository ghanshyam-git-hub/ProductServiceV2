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
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
@Qualifier("RealStoreProductService")
public class RealStoreService implements ProductsService{
    ProductServiceRepo productServiceRepo;
    CategoryServiceRepo categoryServiceRepo;
    RedisTemplate redisTemplate;

    public RealStoreService(ProductServiceRepo productServiceRepo,CategoryServiceRepo categoryServiceRepo,RedisTemplate redisTemplate){
        this.productServiceRepo = productServiceRepo;
        this.categoryServiceRepo = categoryServiceRepo;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public ProductDto getProductsById(Long id) throws NotFoundException {
        //Redis Check
        ProductDto cachedProductDto = (ProductDto) redisTemplate.opsForHash().get("Product","PRODUCT_"+id);
        if(cachedProductDto!=null) return cachedProductDto;

        Product product = productServiceRepo.getProductByProduct_id(id);
        if(product==null) throw new NotFoundException("Requested Product does not exists");

        ProductDto productDto = convertProductToProductDto(product);
        //Redis entry
        redisTemplate.opsForHash().put("Product","PRODUCT_"+id,productDto);
        return productDto;
    }

    @Override
    public List<ProductDto> getAllProducts() throws NotFoundException {
        //Redis check
        List<ProductDto> cachedAllProductsList = (List<ProductDto>) redisTemplate.opsForHash().get("Product","ALL_PRODUCTS");
        if(cachedAllProductsList!=null) return cachedAllProductsList;

        List<Product> productList = productServiceRepo.getAllProducts();
        if(productList.size()==0) throw new NotFoundException("There is no Entry yet!");
        List<ProductDto> productDtoList = productList.stream().map((product)->convertProductToProductDto(product)).toList();

        //Redis entry
        redisTemplate.opsForHash().put("Product","ALL_PRODUCTS",productDtoList);
        return productDtoList;
    }

    @Override
    public List<String> getAllCategories() throws NotFoundException {
        //Redis check
        List<String> cachedCategoriesList = ( List<String>)redisTemplate.opsForHash().get("Category","ALL_CATEGORIES");
        if(cachedCategoriesList!=null) return cachedCategoriesList;

        List<Category> categoryList = categoryServiceRepo.getAllCategories();
        if(categoryList.size()==0)throw new NotFoundException("No Categories added yet");

        List<String>categoryListString = categoryList.stream().map((category)->category.getCategory()).toList();
        // Redis entry
        redisTemplate.opsForHash().put("Category","ALL_CATEGORIES",categoryListString);

        return categoryListString;
    }

    @Override
    public List<ProductDto> getProductsInCategory(String category) throws NotFoundException {
        //redis check
        List<ProductDto> productsInCategoryList = ( List<ProductDto>)redisTemplate.opsForHash().get("Products","CATEGORY_"+category);
        if(productsInCategoryList!=null) return productsInCategoryList;

        List<Product> productList = productServiceRepo.getProductsByCategory_Category(category);
        if(productList.size()==0) throw new NotFoundException("No Products in this Category");
        List<ProductDto> productDtoList = productList.stream().map((product)->convertProductToProductDto(product)).toList();
        //Redis entry
        redisTemplate.opsForHash().put("Products","CATEGORY_"+category,productDtoList);
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

        //redis check
        Page<ProductDto> cachedProductDtoPage = ( Page<ProductDto>)redisTemplate.opsForHash().get("Product","SEARCH_BY_"+pageable);
        if(cachedProductDtoPage!=null) return cachedProductDtoPage;

        Page<Product> productPage = productServiceRepo.findAllBy(pageable);
        Page<ProductDto>productDtoPage = productPage.map((p)-> convertProductToProductDto(p)); // Product should not be send in th o/p else infinite nesting

        redisTemplate.opsForHash().put("Product","SEARCH_BY_"+pageable,productDtoPage);
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
