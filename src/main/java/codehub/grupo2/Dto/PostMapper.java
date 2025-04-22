package codehub.grupo2.Dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

import codehub.grupo2.DB.Entity.Post;
import codehub.grupo2.DB.Entity.Topic;
import codehub.grupo2.DB.Entity.UserName;

import java.util.Collection;
import java.util.List;

@Mapper(componentModel = "spring")
public abstract class PostMapper {

    @Autowired
    protected UserNameMapper userNameMapper;

    @Autowired
    protected TopicMapper topicMapper;

    @Autowired
    protected CommentMapper commentMapper;

    @Mapping(target = "user", expression = "java(userNameMapper.toDTO(post.getUsername()))")
    @Mapping(target = "topic", expression = "java(topicMapper.toDTO(post.getTopic()))")
    @Mapping(target = "comments", expression = "java(commentMapper.toDTOs(post.getComments()))")
    public abstract PostDTO toDTO(Post post);

    @Mapping(target = "user", expression = "java(userNameMapper.toDTO(post.getUsername()))")
    @Mapping(target = "topic", expression = "java(topicMapper.toDTO(post.getTopic()))")
    @Mapping(target = "comments", expression = "java(commentMapper.toDTOs(post.getComments()))")
    public abstract List<PostDTO> toDTOs(Collection<Post> posts);

    @Mapping(target = "topic", ignore = true)
    public abstract Post toDomain(PostDTO postDTO);

    @Mapping(target = "posts", ignore = true)
    public abstract Topic toDomain(TopicDTO topicDTO);

    @Mapping(target = "posts", ignore = true)
    public abstract UserName toDomain(UserNameDTO userNameDTO);
}