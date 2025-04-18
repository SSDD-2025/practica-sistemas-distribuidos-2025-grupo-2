package codehub.grupo2.Dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import codehub.grupo2.DB.Entity.Topic;

import java.util.Collection;
import java.util.List;

@Mapper(componentModel = "spring")
public interface TopicMapper {

    TopicDTO toDTO(Topic Topic);

    List<TopicDTO> toDTOs(Collection<Topic> Topics);

    @Mapping(target = "posts", ignore = true)
    Topic toDomain(TopicDTO TopicDTO);
}
