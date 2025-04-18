package codehub.grupo2.Dto;

import java.util.Collection;
import java.util.List;

import org.mapstruct.Mapper;

import codehub.grupo2.DB.Entity.UserName;



@Mapper(componentModel = "spring")
public interface UserNameMapper {
    UserNameDTO toDTO(UserName user);
    Collection<UserNameDTO> toDTOs(List<UserName> users);
}