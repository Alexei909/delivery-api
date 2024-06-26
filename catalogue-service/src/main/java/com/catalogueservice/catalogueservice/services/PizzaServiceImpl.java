package com.catalogueservice.catalogueservice.services;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.catalogueservice.catalogueservice.dtos.PizzaCreateDto;
import com.catalogueservice.catalogueservice.dtos.PizzaReturnsDto;
import com.catalogueservice.catalogueservice.dtos.PizzaUpdateDto;
import com.catalogueservice.catalogueservice.entity.Pizza;
import com.catalogueservice.catalogueservice.mappers.PizzaMapper;
import com.catalogueservice.catalogueservice.repositories.PizzaRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PizzaServiceImpl implements PizzaService {

    private final PizzaMapper pizzaMapper;
    
    private final PizzaRepository pizzaRepository;

    @Override
    @Transactional
    public PizzaReturnsDto createPizza(PizzaCreateDto dto) {
        return this.pizzaMapper.toDto(this.pizzaRepository
                .save(this.pizzaMapper.toModel(dto)));
    }

    @Override
    public List<PizzaReturnsDto> findAllPizza(String filter) {
        List<Pizza> pizza;

        if (filter != null && !filter.isBlank()) {
            pizza = this.pizzaRepository.findAllByNameLikeIgnoreCase("%" + filter + "%");
        } else {
            pizza = this.pizzaRepository.findAll();
        }
        return pizza.stream()
                .map(pizzaMapper::toDto)
                .toList();
    }

    @Override
    public Optional<PizzaReturnsDto> findPizzaById(Long id) {
        Optional<Pizza> pizza = this.pizzaRepository.findById(id);

        if (pizza.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(this.pizzaMapper.toDto(pizza.get()));
        }
    }

    @Override
    @Transactional
    public void updatePizza(Long pizzaId, PizzaUpdateDto dto) {
        this.pizzaRepository.findById(pizzaId)
                .ifPresentOrElse(pizza -> {
                    if (dto.getName() != null) pizza.setName(dto.getName());
                    if (dto.getComposition() != null) pizza.setComposition(dto.getComposition());
                    if (dto.getPrice() != null) pizza.setPrice(dto.getPrice());
                }, 
                () -> {
                    throw new NoSuchElementException();
                });
    }

    @Override
    @Transactional
    public void deletePizza(Long pizzaId) {
        this.pizzaRepository.deleteById(pizzaId);
    }

}
