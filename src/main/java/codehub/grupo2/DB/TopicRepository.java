package codehub.grupo2.DB;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import codehub.grupo2.DB.Entity.*;

@Repository
public interface TopicRepository extends JpaRepository<Topic, Long> {
    
}