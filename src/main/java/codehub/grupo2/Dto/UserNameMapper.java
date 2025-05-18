package codehub.grupo2.Dto;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import codehub.grupo2.DB.Entity.UserName;

@Mapper(componentModel = "spring")
public abstract class UserNameMapper {

    public abstract UserNameDTO toDTO(UserName user);

    public abstract UserName toDomain(UserNameDTO dto);

    public List<UserNameDTO> toDTOs(List<UserName> users) {
        return users.stream()
            .map(this::toDTO)
            .collect(Collectors.toList());
    }

}