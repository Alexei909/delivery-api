package com.catalogueservice.catalogueservice.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.catalogueservice.catalogueservice.entity.Pizza;

public interface PizzaRepository extends JpaRepository<Pizza, Long> {

    List<Pizza> findAllByNameLikeIgnoreCase(String name);
}
