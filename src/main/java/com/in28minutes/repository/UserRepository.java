package com.in28minutes.repository;

import com.in28minutes.model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserRepository extends CrudRepository<User, Long> {
    List<User> findByRole(String Role);
}