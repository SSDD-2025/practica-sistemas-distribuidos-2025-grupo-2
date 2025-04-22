package codehub.grupo2.DB;

import codehub.grupo2.DB.Entity.Topic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TopicRepository extends JpaRepository<Topic, Long> {
    Topic findByTopicName(String topicName);
}