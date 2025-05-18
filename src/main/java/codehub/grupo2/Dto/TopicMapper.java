package codehub.grupo2.Dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import codehub.grupo2.DB.Entity.Topic;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public class TopicMapper {

    public TopicDTO toDTO(Topic topic) {
        return new TopicDTO(topic.getId(), topic.getTopicName());
    }

    public Topic toDomain(TopicDTO dto) {
        Topic topic = new Topic(dto.topicName());
        topic.setId(dto.id());
        return topic;
    }

    public List<TopicDTO> toDTOs(List<Topic> topics) {
        return topics.stream()
            .map(this::toDTO)
            .collect(Collectors.toList());
    }
}