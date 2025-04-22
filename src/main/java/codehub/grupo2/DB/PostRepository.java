package codehub.grupo2.DB;

import java.util.List;

import org.springdoc.core.converters.models.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import codehub.grupo2.DB.Entity.*;
@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    Post findByTitle(String title);
    List<Post> findByTopic(Topic topic);
    void deleteAllByTopic(Topic topic);
    List<Post> findAll();
    List<Post> findByUserId(Long id); 
    Page<Post> findByUser(UserName user, PageRequest pageable);
}