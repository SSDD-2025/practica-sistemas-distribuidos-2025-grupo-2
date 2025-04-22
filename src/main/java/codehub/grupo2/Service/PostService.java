package codehub.grupo2.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
            throw new IllegalArgumentException("The user cannot be null and must have a valid ID");
        }
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("the title cannot be null or empty");
        }
        if (text == null || text.trim().isEmpty()) {
            throw new IllegalArgumentException("the text cannot be null or empty");
        }
        if (topic == null || topic.id() == null) {
            throw new IllegalArgumentException(" the topic cannot be null and must have a valid ID");
        }
        if (text.length() > 240) {
            throw new IllegalArgumentException("the text cannot exceed 240 characters");
        }
        Post existingPost = PostBD.findByTitle(title);
        if (existingPost != null && existingPost.getTopic().getId().equals(topic.id())) {
            throw new IllegalArgumentException("it already exists a post with the same title in the same topic");
        }
        Topic topicEntity = TopicBD.findById(topic.id())
                .orElseThrow(() -> new EntityNotFoundException("topic with id" + topic.id() + " not found"));

        UserName userEntity = UserBD.findById(user.id())
                .orElseThrow(() -> new EntityNotFoundException("user with id " + user.id() + " not found"));
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
     public Page<PostDTO> getPostsByUserPaginated(UserNameDTO user, int page, int size) {
        if (user == null || user.id() == null) {
            throw new IllegalArgumentException("User cannot be null");
        }
        UserName userEntity = UserBD.findById(user.id())
                .orElseThrow(() -> new EntityNotFoundException("User with id " + user.id() + " not found"));
        PageRequest pageable = PageRequest.of(page, size);
        Page<Post> postPage = PostBD.findByUser(userEntity, pageable);
        return postPage.map(PostMapper::toDTO);
    }

    // Método para obtener todos los posts con paginación (si es necesario)
    public Page<PostDTO> getAllPostDTOPaginated(int page, int size) {
        PageRequest pageable = PageRequest.of(page, size);
        Page<Post> postPage = PostBD.findAll(pageable);
        return postPage.map(PostMapper::toDTO);
    }
}