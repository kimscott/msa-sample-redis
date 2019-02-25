package com.example.redisTemplate;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CustomerRepository extends CrudRepository<Customer, Long> {

    Customer findByUserName(@Param("userName") String userName);
    List<Customer> findByLastName(@Param("lastName") String lastName);
}
