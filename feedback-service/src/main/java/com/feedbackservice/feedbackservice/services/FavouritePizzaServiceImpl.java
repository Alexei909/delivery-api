package com.feedbackservice.feedbackservice.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.feedbackservice.feedbackservice.dtos.FavouritePizzaCreateDto;
import com.feedbackservice.feedbackservice.dtos.FavouritePizzaReturnsDto;
import com.feedbackservice.feedbackservice.mappers.FavouritePizzaMapper;
import com.feedbackservice.feedbackservice.models.FavouritePizza;
import com.feedbackservice.feedbackservice.repositories.FavouritePizzaRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FavouritePizzaServiceImpl implements FavouritePizzaService {

    private final FavouritePizzaRepository favouritePizzaRepository;
    private final FavouritePizzaMapper favouritePizzaMapper;

    @Override
    public List<FavouritePizzaReturnsDto> getAllFavouritePizza() {
        return this.favouritePizzaRepository.findAll().stream()
                .map(this.favouritePizzaMapper::toDto)
                .toList();
    }

    @Override
    public List<FavouritePizzaReturnsDto> getAllFavouritePizzaByPizzaId(Long pizzaId) {
        return this.favouritePizzaRepository.findAllByPizzaId(pizzaId).stream()
                .map(this.favouritePizzaMapper::toDto)
                .toList();
    }

    @Override
    public Optional<FavouritePizzaReturnsDto> getFavouritePizzaById(Long id) {
        Optional<FavouritePizza> pizza = this.favouritePizzaRepository.findById(id);

        return (pizza.isEmpty()) ? Optional.empty() : 
                Optional.of(this.favouritePizzaMapper.toDto(pizza.get()));
                
    }

    @Override
    public FavouritePizzaReturnsDto createFavouritePizza(FavouritePizzaCreateDto dto) {
        FavouritePizza pizza = this.favouritePizzaRepository.save(
                new FavouritePizza(null, dto.getPizzaId(), null, null));
        return this.favouritePizzaMapper.toDto(pizza);
    }

    @Override
    public void deleteFavouritePizza(Long id) {
        this.favouritePizzaRepository.deleteById(id);
    }

}
