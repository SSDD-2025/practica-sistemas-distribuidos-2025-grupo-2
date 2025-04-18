package codehub.grupo2.Dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import codehub.grupo2.DB.Entity.Comment;
import codehub.grupo2.DB.Entity.Post;

import codehub.grupo2.DB.Entity.UserName;

import java.util.Collection;
import java.util.List;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    CommentDTO toDTO(Comment Comment);

    List<CommentDTO> toDTOs(Collection<Comment> Comments);

    @Mapping(target = "topic", ignore = true)
    Comment toDomain(CommentDTO CommentDTO);

    @Mapping(target = "Comments", ignore = true)
    Post toDomain(PostDTO PostDTO); 

    @Mapping(target = "Comments", ignore = true)
    UserName toDomain(UserNameDTO UserNameDTO);
}


