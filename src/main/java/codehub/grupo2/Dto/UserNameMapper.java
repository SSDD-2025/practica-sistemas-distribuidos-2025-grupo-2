package codehub.grupo2.Dto;

import java.util.Collection;
import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import codehub.grupo2.DB.Entity.UserName;

@Mapper(componentModel = "spring")
public interface UserNameMapper {

    @Mapping(target = "profilePicture", ignore = true)
    @Mapping(target = "posts", ignore = true)
    UserNameDTO toDTO(UserName userName);

    @Mapping(target = "posts", ignore = true)
    UserName toDomain(UserNameDTO userNameDTO);

    Collection<UserNameDTO> toDTOs(List<UserName> users);

}