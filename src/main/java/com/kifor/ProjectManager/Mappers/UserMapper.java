package com.kifor.ProjectManager.Mappers;


import com.kifor.ProjectManager.DTO.UserDTO;
import com.kifor.ProjectManager.Entities.User.User;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDTO toDTO(User user);
}
