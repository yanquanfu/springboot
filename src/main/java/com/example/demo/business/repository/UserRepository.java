package com.example.demo.business.repository;

import com.example.demo.business.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserRepository extends CrudRepository<User,Integer> {

    @Query(value = "select r from User r where r.isDel=0")
    List<User> findCurrentUser(Pageable pageable);

    @Query(value = "select count(r) from User r where r.isDel=0")
    Integer countFindCurrentUser();

    @Query(value = "select r from User r where r.isDel=0")
    List<User> findAllUser();
}
