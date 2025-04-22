package codehub.grupo2.Service.APIService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import codehub.grupo2.DB.PostRepository;
import codehub.grupo2.DB.TopicRepository;
import codehub.grupo2.DB.UserRepository;
import codehub.grupo2.DB.Entity.Post;
import codehub.grupo2.DB.Entity.Topic;
import codehub.grupo2.DB.Entity.UserName;
import codehub.grupo2.Dto.PostDTO;
import codehub.grupo2.Dto.PostMapper;
@Service
public class APIPostService {
     @Autowired 
    private PostMapper postMapper;

    @Autowired
    private TopicRepository TopicBD;

    @Autowired
    private PostRepository PostBD;

    @Autowired
    private UserRepository UserBD;
    public Collection<PostDTO> getAllPostDTO() {
        List<Post> posts = PostBD.findAll();
        return postMapper.toDTOs(posts);
    }

    public PostDTO getPostByIdDTO(long id) {
        Post post = PostBD.findById(id).orElse(null);
        if (post != null) {
            return postMapper.toDTO(post);
        } else {
            return null;
        }
    }

public PostDTO registerPostDTO(UserName user, String title, String text, Topic topic) {
    if (text.length() > 240) {
        throw new IllegalArgumentException("Too many characters in the text.");
    }
    Post existingPost = PostBD.findByTitle(title);
    if (existingPost != null && existingPost.getTopic().equals(topic)) {
        throw new IllegalArgumentException("The post is already associated with this topic.");
    }
    if (topic.getId() == null) {
        TopicBD.save(topic);
    }
    Post post = new Post(user, title, text, topic);
    Post savedPost = PostBD.save(post);

    if (user.getPosts() == null) {
        user.setPosts(new ArrayList<>());
    }
    user.getPosts().add(savedPost);
    UserBD.save(user);

    if (topic.getPosts() == null) {
        topic.setPosts(new ArrayList<>());
    }
    topic.getPosts().add(savedPost);
    TopicBD.save(topic);

    return postMapper.toDTO(savedPost);
}

    public PostDTO deletePostDTO(String title) {
        Post post = PostBD.findByTitle(title);
        if (post != null) {
            UserName user = post.getUsername();
            Topic topic = post.getTopic();

            user.getPosts().remove(post);
            topic.getPosts().remove(post); 

            PostBD.delete(post); 

            UserBD.save(user); 
            TopicBD.save(topic);

            return postMapper.toDTO(post);
        } else {
            return null;
        }
    }
}


