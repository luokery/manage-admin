package com.example.manageadmin.mapper;

import com.example.manageadmin.model.po.User;
import com.example.manageadmin.model.dto.auth.RegisterDTO;
import com.example.manageadmin.model.dto.user.UserCreateDTO;
import com.example.manageadmin.model.dto.user.UserResponseDTO;
import com.example.manageadmin.model.dto.user.UserUpdateDTO;

import org.mapstruct.*;
import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", constant = "1")
    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "updatedAt", expression = "java(java.time.LocalDateTime.now())")
    User toEntity(UserCreateDTO dto);
    
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "updatedAt", expression = "java(java.time.LocalDateTime.now())")
    void updateEntityFromDTO(UserUpdateDTO dto, @MappingTarget User entity);
    
    UserResponseDTO toResponseDTO(User entity);
    
    List<UserResponseDTO> toResponseDTOList(List<User> entities);
    
	//====================================================
	UserCreateDTO toUserCreateDTO(RegisterDTO registerDTO);

	UserResponseDTO toResponseDTO(UserCreateDTO userDTO);
}
