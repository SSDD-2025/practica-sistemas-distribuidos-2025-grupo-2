package codehub.grupo2.Dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import codehub.grupo2.DB.Entity.Comment;
import codehub.grupo2.DB.Entity.Post;

import java.util.Collection;
import java.util.List;

@Mapper(componentModel = "spring", uses = {UserNameMapper.class})
public interface CommentMapper {
    @Mapping(target = "post", ignore = true)
    @Mapping(target = "user", expression = "java(userNameMapper.toDTO(comment.getUser()))")
    CommentDTO toDTO(Comment comment);

    @Mapping(target = "post", ignore = true)
    List<CommentDTO> toDTOs(Collection<Comment> comments);

    @Mapping(target = "post", ignore = true)
    Comment toDomain(CommentDTO commentDTO);

    @Mapping(target = "comments", ignore = true)
    Post toDomain(PostDTO postDTO);
}