package com.example.manageadmin.mapper;

import com.example.manageadmin.model.vo.auth.LoginVO;
import com.example.manageadmin.model.vo.user.UserVO;

import jakarta.validation.Valid;

import com.example.manageadmin.model.dto.auth.LoginDTO;
import com.example.manageadmin.model.dto.user.UserResponseDTO;

import org.mapstruct.*;
import org.mapstruct.factory.Mappers;


@Mapper(componentModel = "spring")
public interface AuthMapper {
    
	AuthMapper INSTANCE = Mappers.getMapper( AuthMapper.class );

	LoginDTO toLoginDTO(@Valid LoginVO loginVO);

	UserVO toUserVO(UserResponseDTO userDTO);

}
