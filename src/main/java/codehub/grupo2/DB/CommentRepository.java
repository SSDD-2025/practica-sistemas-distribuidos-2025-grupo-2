package codehub.grupo2.DB;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import codehub.grupo2.DB.Entity.*;
@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
   Comment findByTitle(String title);
   Comment findById(long id);
}
