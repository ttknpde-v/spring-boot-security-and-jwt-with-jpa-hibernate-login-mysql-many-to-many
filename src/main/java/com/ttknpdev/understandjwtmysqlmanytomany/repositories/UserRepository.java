package com.ttknpdev.understandjwtmysqlmanytomany.repositories;

import com.ttknpdev.understandjwtmysqlmanytomany.entities.relations.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByName(String name);

    Boolean existsByEmail(String email);

    Optional<User> findByNameOrEmail(String name, String email);
    boolean existsByName(String username);
}