package com.authservice.authservice.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.authservice.authservice.dtos.UserReturnsDto;
import com.authservice.authservice.models.UserEntities;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserMapper INSTANSE = Mappers.getMapper(UserMapper.class);
    
    @Mapping(target = "roles", expression = 
            "java(user.getRoles().stream().map(role -> role.getName()).collect(java.util.stream.Collectors.toSet()))")
    UserReturnsDto toDto(UserEntities user);
}
