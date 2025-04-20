package codehub.grupo2.Service;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import codehub.grupo2.DB.CommentRepository;
import codehub.grupo2.DB.Entity.Comment;
import codehub.grupo2.DB.Entity.Post;
import codehub.grupo2.DB.Entity.UserName;

@Service
public class CommentService {

    
    @Autowired
    private CommentRepository CommentBD;

    public Comment getCommentById(long id) {
        return CommentBD.findById(id);
    }
    
     public String registerComment(UserName user , String text, Post post){
        if(text.length() > 140){
            return "Numero de caracteres escedido";
        }
        Comment comment = new Comment(user,text,post);
        CommentBD.save(comment);
        post.getComments().add(comment);
        return text;
    }

    public List<Comment> getAllComments(){ 
        return CommentBD.findAll();
    }

    @Transactional
    public int deleteComment(Long id) {
        Optional<Comment> c = CommentBD.findById(id);
        if (c.isPresent()) {
            CommentBD.delete(c.get());
            return 0;
        } else {
            return 1;
        }
    }
     

    public List<Comment> getCommentsByUserId(String name) {
        return CommentBD.findByUser_Username(name);
    }

    @Transactional
    public void deleteCommentsByUser(long userId) {
        CommentBD.deleteByUserId(userId);
    }

    public List<Map<String, Object>> getCommentsWithOwnership(List<Comment> comments, UserName user) {
        return comments.stream().map(comment -> {
            Map<String, Object> data = new HashMap<>();
            data.put("comment", comment);
            data.put("isOwner", user != null && comment.getUsername().getId().equals(user.getId()));
            return data;
        }).collect(Collectors.toList());
    }

    public Map<String, Object> getCommentWithOwnership(Comment comment, UserName user) {
        Map<String, Object> data = new HashMap<>();
        data.put("comment", comment);
        data.put("isOwner", user != null && comment.getUsername().getId().equals(user.getId()));
        return data;
    }
    
}
