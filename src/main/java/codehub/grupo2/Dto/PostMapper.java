package codehub.grupo2.Dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import codehub.grupo2.DB.Entity.Post;
import codehub.grupo2.DB.Entity.Topic;

import java.util.Collection;
import java.util.List;

@Mapper(componentModel = "spring")
public interface PostMapper {

    @Mapping(target = "topic", ignore = true)
    @Mapping(target = "comments", ignore = true)
    PostDTO toDTO(Post Post);

    @Mapping(target = "topic", ignore = true)
    @Mapping(target = "comments", ignore = true)
    List<PostDTO> toDTOs(Collection<Post> Posts);

    @Mapping(target = "topic", ignore = true)
    Post toDomain(PostDTO PostDTO);

    @Mapping(target = "posts", ignore = true)
    Topic toDomain(TopicDTO TopicDTO);
}
