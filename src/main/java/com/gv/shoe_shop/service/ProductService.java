package com.gv.shoe_shop.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.gv.shoe_shop.dto.request.ProductRequest;
import com.gv.shoe_shop.entity.Product;
import com.gv.shoe_shop.entity.Category;
import com.gv.shoe_shop.exception.CustomRuntimeException;
import com.gv.shoe_shop.repository.CategoryRepository;
import com.gv.shoe_shop.repository.ProductRepository;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    private static final String IMAGE_UPLOAD_DIR = "src/main/resources/static/assets/web/img/product";

    public List<Product> getAllProducts() {
        return productRepository.findByDeletedDateIsNull();
    }

    public Product getProductById(String id){
        return productRepository.findById(id)
                .orElseThrow(() -> new CustomRuntimeException("Product not found"));
    }

    public Product createProduct(ProductRequest productRequest) throws IOException{
        try {
            Category category = categoryRepository.findById(productRequest.getCategoryid())
                    .orElseThrow(()-> new CustomRuntimeException("Category not found"));
            Product product = new Product();
            product.setId(productRequest.getId());
            product.setName(productRequest.getName());
            product.setPrice(productRequest.getPrice());
            product.setQuantity(productRequest.getQuantity());
            product.setDescription(productRequest.getDescription());
            product.setCreatedDate(LocalDateTime.now());
            product.setProductCategory(category);

            MultipartFile imageFile = productRequest.getImage();

            if (imageFile != null && !imageFile.isEmpty()) {
                String imageName = imageFile.getOriginalFilename();
                Path imagePath = Paths.get(IMAGE_UPLOAD_DIR, imageName);
                Files.write(imagePath, imageFile.getBytes());
                product.setImage(imageName); 
            }
            return productRepository.save(product);
        } catch (Exception e) {
            throw new CustomRuntimeException("Could not save image file", e);
        }
    }

    public Product updateProduct(String id, ProductRequest productRequest) throws IOException {
        Product product = productRepository.findById(id)
                .orElseThrow(()-> new CustomRuntimeException("Product not found"));

        Category category = categoryRepository.findById(productRequest.getCategoryid())
                .orElseThrow(()-> new CustomRuntimeException("Category not found"));

        if(product != null){
            product.setName(productRequest.getName());
            product.setPrice(productRequest.getPrice());
            product.setQuantity(productRequest.getQuantity());
            product.setDescription(productRequest.getDescription());
            product.setProductCategory(category);

            MultipartFile imageFile = productRequest.getImage();

            if (imageFile != null && !imageFile.isEmpty()) {
                String imageName = imageFile.getOriginalFilename();
                String imageNameWithoutExt = imageName.substring(0, imageName.lastIndexOf('.'));
                String extension = imageName.substring(imageName.lastIndexOf('.'));

                Path imagePath = Paths.get(IMAGE_UPLOAD_DIR, imageName);
                int count = 1;

                // Kiểm tra nếu file tồn tại, thêm số vào tên file
                while (Files.exists(imagePath)) {
                    imageName = imageNameWithoutExt + "_" + count + extension;
                    imagePath = Paths.get(IMAGE_UPLOAD_DIR, imageName);
                    count++;
                }

                Files.write(imagePath, imageFile.getBytes());
                product.setImage(imageName);  // Lưu tên file ảnh vào cơ sở dữ liệu
            }

            return productRepository.save(product);
        }else {
            throw new CustomRuntimeException("Product not found with id: " + id);
        }
    }

    // public void saveImage(MultipartFile file, String name){
    //     try{
    //         String cleanImageName = StringUtils.cleanPath(name);
    //         Path path = Paths.get(imagePath + cleanImageName);
    //         file.transferTo(new File(path.toString()));
    //     }catch(IOException e){
    //         e.printStackTrace();
    //     }
    // }

    public void softDelete(String id){
        Product product = productRepository.findById(id)
                .orElseThrow(()-> new CustomRuntimeException("Product not found"));

        if(product != null){
            product.setDeletedDate(LocalDateTime.now());
            productRepository.save(product);
        }
    }

    public List<Product> getTopProducts() {
        return productRepository.findByDeletedDateIsNull(Sort.by(Sort.Order.desc("createdDate")))
                .stream()
                .limit(8) // Chỉ lấy 8 sản phẩm
                .collect(Collectors.toList());
    }

    public List<Product> getProductsByCategoryId(String categoryId) {
        return productRepository.findByProductCategoryId(categoryId);
    }

}
