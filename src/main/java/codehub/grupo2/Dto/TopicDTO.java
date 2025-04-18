package codehub.grupo2.Dto;

import java.util.List;

import codehub.grupo2.DB.Entity.Post;

public record TopicDTO(
    Long id,
    String topicName,
    List<Post> posts
    ) {}
