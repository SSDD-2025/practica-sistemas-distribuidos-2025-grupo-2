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
    
    UserName toDomain(UserNameDTO user);

    UserNameDTO toDTO(UserName user);

    Collection<UserNameDTO> toDTOs(List<UserName> users);

}