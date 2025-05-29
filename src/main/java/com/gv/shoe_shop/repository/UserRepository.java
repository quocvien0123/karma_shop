package com.gv.shoe_shop.repository;

import com.gv.shoe_shop.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByUsername(String username);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    List<User> findByRoles(Set<String> roles);
    List<User> findByRolesAndUsernameContainingIgnoreCase(Set<String> roles, String username);
}
