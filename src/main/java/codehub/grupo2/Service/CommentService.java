package codehub.grupo2.Service;
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
import codehub.grupo2.Dto.PostDTO;
import codehub.grupo2.Dto.PostMapper;
import codehub.grupo2.Dto.TopicMapper;
import codehub.grupo2.Dto.UserNameDTO;
import codehub.grupo2.Dto.UserNameMapper;
import jakarta.persistence.EntityNotFoundException;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private PostMapper postMapper;

    @Autowired
    private UserRepository userBD;

    @Autowired
    private PostRepository PostBD;

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private TopicMapper topicMapper;
     
    @Autowired
    private CommentRepository CommentBD;

    @Autowired
    private UserNameMapper userNameMapper;

    @Transactional
    public int deleteComment(Long id) {
        if (CommentBD.existsById(id)) {
            CommentBD.deleteById(id);
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
    public int deleteCommentByUserAPI(long commentId, String username) {
        UserName user = userBD.findByUsername(username);
        CommentDTO comment = getCommentByIdDTO(commentId);
        if (comment == null) {
            return 1; 
        }
        if (comment.user().id() != user.getId()) {
            return 2;
        }
        deleteComment(commentId);
        return 0;
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
        if (commentDTO == null) {
            throw new IllegalArgumentException("the comment cannot be null");
        }
        if (commentDTO.text() == null || commentDTO.text().trim().isEmpty()) {
            throw new IllegalArgumentException("text cannot be null or empty");
        }
        if (commentDTO.user() == null || commentDTO.user().id() == null) {
            throw new IllegalArgumentException("user cannot be null and must have a valid ID");
        }
        if (commentDTO.postId() == null || commentDTO.postId() == null) {
            throw new IllegalArgumentException("post cannot be null and must have a valid ID");
        }
        if (commentDTO.text().length() > 140) {
            throw new IllegalArgumentException("text cannot exceed 140 characters");
        }
        UserName user = userBD.findById(commentDTO.user().id())
                .orElseThrow(() -> new EntityNotFoundException("user with id " + commentDTO.user().id() + " not found"));
        Post post = PostBD.findById(commentDTO.postId())
                .orElseThrow(() -> new EntityNotFoundException("post with id " + commentDTO.postId() + " not found"));
        Comment comment = new Comment(user, commentDTO.text(), post);
        Comment savedComment = CommentBD.save(comment);
        post.getComments().add(savedComment);
        PostBD.save(post);
        return commentMapper.toDTO(savedComment);
    }

public List<CommentDTO> getCommentsByPostId(Long postId) {
    List<Comment> comments = commentRepository.findByPostId(postId);
    return comments.stream()
        .map(commentMapper::toDTO)
        .collect(Collectors.toList());
}
    
}
