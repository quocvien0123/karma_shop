package com.gv.shoe_shop.repository;


import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.gv.shoe_shop.entity.Category;

@Repository
public interface CategoryRepository extends MongoRepository<Category, String>{
    List<Category> findByDeletedDateIsNull();
}
