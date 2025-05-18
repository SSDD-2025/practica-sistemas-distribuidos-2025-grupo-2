package codehub.grupo2.Dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

import codehub.grupo2.DB.Entity.Post;
import codehub.grupo2.DB.Entity.Topic;
import codehub.grupo2.DB.Entity.UserName;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public abstract class PostMapper {

    @Autowired
    protected UserNameMapper userNameMapper;

    @Autowired
    protected TopicMapper topicMapper;

    public PostDTO toDTO(Post post) {
        return new PostDTO(
            post.getId(),
            post.getTitle(),
            post.getText(),
            post.getDate(),
            userNameMapper.toDTO(post.getUser()),
            topicMapper.toDTO(post.getTopic())
        );
    }

    public Post toDomain(PostDTO dto) {
        Post post = new Post(
            userNameMapper.toDomain(dto.user()),
            dto.title(),
            dto.text(),
            topicMapper.toDomain(dto.topic())
        );
        post.setId(dto.id());
        post.setDate(dto.date());
        post.setComments(new ArrayList<>());
        return post;
    }

    public List<PostDTO> toDTOs(List<Post> posts) {
        return posts.stream()
            .map(this::toDTO)
            .collect(Collectors.toList());
    }
}