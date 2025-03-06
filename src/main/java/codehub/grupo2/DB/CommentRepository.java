package codehub.grupo2.DB;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import codehub.grupo2.DB.Entity.*;
@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
   Comment findById(long id);
   List<Comment> findByUser_Username(String username);
   void deleteByUserId(Long userId);
}
