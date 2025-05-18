package codehub.grupo2.Dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

import codehub.grupo2.DB.Entity.Comment;
import codehub.grupo2.DB.Entity.Post;
import codehub.grupo2.Service.PostService;

import java.util.Collection;
import java.util.List;

@Mapper(componentModel = "spring")
public abstract class CommentMapper {

    @Autowired
    protected UserNameMapper userNameMapper;

    @Autowired
    protected PostService postService;

    @Autowired
    protected PostMapper postMapper;

    public CommentDTO toDTO(Comment comment) {
        return new CommentDTO(
            comment.getId(),
            comment.getText(),
            comment.getDate(),
            userNameMapper.toDTO(comment.getUser()),
            comment.getPost().getId()
        );
    }

    public Comment toDomain(CommentDTO dto) {
        Comment comment = new Comment(
            userNameMapper.toDomain(dto.user()),
            dto.text(),
            postMapper.toDomain(postService.getPostByIdDTO(dto.postId()))
);
        comment.setDate(dto.date());
        return comment;
    }
public List<CommentDTO> toDTOs(Collection<Comment> comments) {
    if (comments == null) {
        return List.of();
    }
    return comments.stream().map(this::toDTO).toList();
}
}

