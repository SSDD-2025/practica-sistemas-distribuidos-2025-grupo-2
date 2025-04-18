package codehub.grupo2.Dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import codehub.grupo2.DB.Entity.Topic;

import java.util.Collection;
import java.util.List;

@Mapper(componentModel = "spring")
public interface TopicMapper {
    @Mapping(target = "posts", ignore = true)
    TopicDTO toDTO(Topic Topic);
    @Mapping(target = "posts", ignore = true)
    List<TopicDTO> toDTOs(Collection<Topic> Topics);

    @Mapping(target = "posts", ignore = true)
    Topic toDomain(TopicDTO TopicDTO);
}
