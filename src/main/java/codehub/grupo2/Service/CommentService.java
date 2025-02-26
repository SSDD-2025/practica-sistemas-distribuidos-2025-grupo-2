package codehub.grupo2.Service;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import codehub.grupo2.DB.CommentRepository;
import codehub.grupo2.DB.Entity.Comment;
import codehub.grupo2.DB.Entity.Post;
import codehub.grupo2.DB.Entity.UserName;

@Service
public class CommentService {
    @Autowired
    private CommentRepository CommentBD;

    public Comment getComment(String title){
        return CommentBD.findByTitle(title);
    }
     public String registerComment(UserName user ,String title,String text, Post post){
        if(text.length() > 140){
            return "Numero de caracteres escedido";
        }
        Comment comment = new Comment(user,title,text,post);
        CommentBD.save(comment);
        post.getComments().add(comment);
        return title;
    }
    public List<Comment> getAllComments(){
        return CommentBD.findAll();
    }
     public void deleteComment(String title){
        Comment comment = CommentBD.findByTitle(title);
        CommentBD.delete(comment);
    }   
}
