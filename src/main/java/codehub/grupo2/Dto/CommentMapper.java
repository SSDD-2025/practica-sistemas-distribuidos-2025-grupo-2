package codehub.grupo2.Dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import codehub.grupo2.DB.Entity.Comment;
import codehub.grupo2.DB.Entity.Post;


import java.util.Collection;
import java.util.List;

@Mapper(componentModel = "spring")
public interface CommentMapper {
    @Mapping(target = "post", ignore = true)
    CommentDTO toDTO(Comment Comment);
    @Mapping(target = "post", ignore = true)
    List<CommentDTO> toDTOs(Collection<Comment> Comments);

    @Mapping(target = "post", ignore = true)
    Comment toDomain(CommentDTO CommentDTO);

    @Mapping(target = "comments", ignore = true)
    Post toDomain(PostDTO PostDTO);

}


