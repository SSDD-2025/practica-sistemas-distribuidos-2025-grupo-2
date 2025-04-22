package codehub.grupo2.Service;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

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
import codehub.grupo2.Dto.UserNameDTO;
import codehub.grupo2.Dto.UserNameMapper;

@Service
public class CommentService {

    @Autowired
    private UserRepository userBD;

    @Autowired
    private PostRepository PostBD;

    @Autowired
    private CommentMapper commentMapper;
     
    @Autowired
    private CommentRepository CommentBD;

    @Autowired
    private UserNameMapper userNameMapper;

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

    public List<CommentDTO> getCommentsByUserId(String name) {
        List<Comment> comments = CommentBD.findByUser_Username(name);
        return commentMapper.toDTOs(comments);
    }

    @Transactional
    public void deleteCommentsByUser(long userId) {
        CommentBD.deleteByUserId(userId);
    }
    
    
    public List<Map<String, Object>> getCommentsWithOwnership(List<CommentDTO> comments, UserNameDTO user) {
        if (user == null) {
            return comments.stream().map(comment -> {
                Map<String, Object> data = new HashMap<>();
                data.put("comment", comment);
                data.put("isOwner", false);
                return data;
            }).collect(Collectors.toList());
        }
        return comments.stream().map(comment -> {
            Map<String, Object> data = new HashMap<>();
            data.put("comment", comment);
            data.put("isOwner", comment.user() != null && user.id() != null &&
                                comment.user().id().equals(user.id()));
            return data;
        }).collect(Collectors.toList());
    }
    
    public Map<String, Object> getCommentWithOwnership(CommentDTO comment, UserNameDTO user) {
        Map<String, Object> data = new HashMap<>();
        data.put("comment", comment);
        data.put("isOwner", user != null && comment.user() != null &&
                            comment.user().id().equals(user.id()));
        return data;
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
    @Transactional
    public CommentDTO registerCommentDTO(CommentDTO commentDTO) {
        if (commentDTO.text() == null) {
            throw new IllegalArgumentException("El texto del comentario no puede ser nulo");
        }
        if (commentDTO.user() == null || commentDTO.user().id() == null) {
            throw new IllegalArgumentException("El usuario no puede ser nulo");
        }
        if (commentDTO.post() == null || commentDTO.post().id() == null) {
            throw new IllegalArgumentException("El post no puede ser nulo");
        }
        if (commentDTO.text().length() > 140) {
            throw new IllegalArgumentException("Número de caracteres excedido");
        }
        UserName user = userBD.findById(commentDTO.user().id())
        .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        Post post = PostBD.findById(commentDTO.post().id())
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
    
}
