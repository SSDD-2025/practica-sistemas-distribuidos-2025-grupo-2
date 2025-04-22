package codehub.grupo2.Service.APIService;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import codehub.grupo2.DB.CommentRepository;
import codehub.grupo2.DB.PostRepository;
import codehub.grupo2.DB.UserRepository;
import codehub.grupo2.DB.Entity.Comment;
import codehub.grupo2.DB.Entity.Post;
import codehub.grupo2.DB.Entity.UserName;
import codehub.grupo2.Dto.CommentDTO;
import codehub.grupo2.Dto.CommentMapper;

@Service
public class APICommentService {
     @Autowired
     private CommentMapper commentMapper;

     @Autowired
     private CommentRepository CommentBD;
     

     @Autowired
     private UserRepository userBD;


     @Autowired
     private PostRepository PostBD;

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
    @Transactional
    public CommentDTO registerCommentDTO(CommentDTO commentDTO) {
        if (commentDTO.text() == null) {
            throw new IllegalArgumentException("El texto del comentario no puede ser nulo");
        }
        if (commentDTO.user() == null || commentDTO.user().getId() == null) {
            throw new IllegalArgumentException("El usuario no puede ser nulo");
        }
        if (commentDTO.post() == null || commentDTO.post().getId() == null) {
            throw new IllegalArgumentException("El post no puede ser nulo");
        }
        if (commentDTO.text().length() > 140) {
            throw new IllegalArgumentException("Número de caracteres excedido");
        }
        UserName user = userBD.findById(commentDTO.user().getId())
        .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        Post post = PostBD.findById(commentDTO.post().getId())
        .orElseThrow(() -> new RuntimeException("Post no encontrado"));
       
        Comment comment = new Comment(user, commentDTO.text(), post);
        comment.setText(commentDTO.text());
        comment.setUsername(user);
        comment.setPost(post);
        CommentBD.save(comment);

        post.getComments().add(comment);
        PostBD.save(post);
        return commentMapper.toDTO(comment);
    }
    @Transactional
    public String registerComment(UserName user, String text, Post post) {
        if (text == null) {
            throw new IllegalArgumentException("El texto del comentario no puede ser nulo");
        }
        if (user == null) {
            throw new IllegalArgumentException("El usuario no puede ser nulo");
        }
        if (post == null) {
            throw new IllegalArgumentException("El post no puede ser nulo");
        }
        if (text.length() > 140) {
            return "Número de caracteres excedido";
        }

        Comment comment = new Comment(user, text, post);
        comment.setText(text);
        comment.setUsername(user);
        comment.setPost(post);

        CommentBD.save(comment);

        
        post.getComments().add(comment);

        PostBD.save(post);

        return text;
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
}
