package com.catalogueservice.catalogueservice.mappers;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.catalogueservice.catalogueservice.dtos.PizzaCreateDto;
import com.catalogueservice.catalogueservice.dtos.PizzaReturnsDto;
import com.catalogueservice.catalogueservice.entity.Pizza;

@Mapper(componentModel = "spring")
public interface PizzaMapper {
    PizzaMapper INSTANSE = Mappers.getMapper(PizzaMapper.class);

    @Mapping(target = "updatedAt", source = "updatedAt")
    @Mapping(target = "createdAt", source = "createdAt")
    PizzaReturnsDto toDto(Pizza pizza);

    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    Pizza toModel(PizzaCreateDto pizzaCreateDto);
}
