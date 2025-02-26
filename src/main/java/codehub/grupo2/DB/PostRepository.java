package codehub.grupo2.DB;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import codehub.grupo2.DB.Entity.*;

public interface PostRepository extends JpaRepository<Post, Long> {
    Post findByTitle(String title);
    List<Post> findByTopic(Topic topic);
}