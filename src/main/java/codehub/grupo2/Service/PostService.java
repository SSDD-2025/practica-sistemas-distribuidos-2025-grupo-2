package codehub.grupo2.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

import codehub.grupo2.Dto.PostMapper;
import codehub.grupo2.Dto.UserNameMapper;
import codehub.grupo2.Dto.TopicDTO;
import codehub.grupo2.Dto.UserNameDTO;
import codehub.grupo2.DB.PostRepository;
import codehub.grupo2.DB.TopicRepository;
import codehub.grupo2.DB.UserRepository;
import codehub.grupo2.DB.Entity.Post;
import codehub.grupo2.DB.Entity.Topic;
import codehub.grupo2.DB.Entity.UserName;
import codehub.grupo2.Dto.PostDTO;

@Service
public class PostService {
    @Autowired
    private TopicRepository TopicBD;

    @Autowired
    private PostRepository PostBD;

    @Autowired
    private UserRepository UserBD;
     
    @Autowired
    private PostMapper PostMapper;

    @Autowired
    private UserNameMapper UserMapper;

    public PostDTO getPostDTO(String title) {
        Post post = PostBD.findByTitle(title);
        return PostMapper.toDTO(post);
    }

    public List<PostDTO> getPostByTopicDTO(TopicDTO topic) {
        if (topic == null || topic.id() == null) {
            throw new IllegalArgumentException("TopicDTO or its ID cannot be null");
        }
        Optional<Topic> topicEntity = TopicBD.findById(topic.id());
        List<Post> list = PostBD.findByTopic(topicEntity.get());
        return PostMapper.toDTOs(list);
    }

    public List<PostDTO> findPostsByRegexDTO(String regex) {
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        List<Post> lis = PostBD.findAll().stream()
                .filter(post -> pattern.matcher(post.getText()).find() || pattern.matcher(post.getTitle()).find())
                .collect(Collectors.toList());
        return PostMapper.toDTOs(lis);
    }

    public Map<String, Object> getPostWithOwnership(PostDTO post, UserNameDTO user) {
        Map<String, Object> data = new HashMap<>();
        data.put("post", post);
        boolean isOwner = user != null && post.user() != null && post.user().id().equals(user.id());
        data.put("isOwner", isOwner);
        return data;
    }

    public List<Map<String, Object>> getPostsWithOwnership(List<PostDTO> posts, UserNameDTO user) {
        return posts.stream().map(post -> {
            Map<String, Object> data = new HashMap<>();
            data.put("post", post);
            boolean isOwner = user != null && post.user() != null && post.user().id().equals(user.id());
            data.put("isOwner", isOwner);
            return data;
        }).collect(Collectors.toList());
    }

    public List<PostDTO> getAllPostDTO() {
        List<Post> posts = PostBD.findAll();
        return PostMapper.toDTOs(posts);
    }

    public PostDTO getPostByIdDTO(long id) {
        Post post = PostBD.findById(id).orElse(null);
        return PostMapper.toDTO(post);
    }

   @Transactional
    public PostDTO registerPostDTO(UserNameDTO user, String title, String text, TopicDTO topic) {
        if (user == null || user.id() == null) {
            throw new IllegalArgumentException("El usuario no puede ser nulo y debe tener un ID válido");
        }
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("El título no puede ser nulo o vacío");
        }
        if (text == null || text.trim().isEmpty()) {
            throw new IllegalArgumentException("El texto no puede ser nulo o vacío");
        }
        if (topic == null || topic.id() == null) {
            throw new IllegalArgumentException("El tema no puede ser nulo y debe tener un ID válido");
        }
        if (text.length() > 240) {
            throw new IllegalArgumentException("El texto excede el límite de 240 caracteres");
        }
        Post existingPost = PostBD.findByTitle(title);
        if (existingPost != null && existingPost.getTopic().getId().equals(topic.id())) {
            throw new IllegalArgumentException("Ya existe una publicación con este título en el tema especificado");
        }
        Topic topicEntity = TopicBD.findById(topic.id())
                .orElseThrow(() -> new EntityNotFoundException("Tema con ID " + topic.id() + " no encontrado"));

        UserName userEntity = UserBD.findById(user.id())
                .orElseThrow(() -> new EntityNotFoundException("Usuario con ID " + user.id() + " no encontrado"));
        Post post = new Post(userEntity, title, text, topicEntity);
        Post savedPost = PostBD.save(post); 
        return PostMapper.toDTO(savedPost);
    }

    @Transactional
    public PostDTO deletePostDTO(String title) {
        Post post = PostBD.findByTitle(title);
        if (post != null) {
            PostBD.delete(post);
            return PostMapper.toDTO(post);
        }
        return null;
    }
}