package codehub.grupo2.Service;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import codehub.grupo2.DB.CommentRepository;
import codehub.grupo2.DB.Entity.Comment;
import codehub.grupo2.DB.Entity.Post;
import codehub.grupo2.DB.Entity.UserName;
import codehub.grupo2.Dto.CommentDTO;
import codehub.grupo2.Dto.CommentMapper;

@Service
public class CommentService {

    @Autowired
    private CommentMapper commentMapper;

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

    public Collection<CommentDTO> getAllCommentsDTO() {
        List<Comment> comments = CommentBD.findAll();
        return commentMapper.toDTOs(comments);
    }

    public CommentDTO getCommentByIdDTO(long id) {
        Comment comment = CommentBD.findById(id);
        if (comment != null) {
            return commentMapper.toDTO(comment);
        } else {
            return null;
        }
    }
    
}
