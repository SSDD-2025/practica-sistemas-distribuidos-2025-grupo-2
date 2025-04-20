package codehub.grupo2.Service;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import codehub.grupo2.DB.PostRepository;
import codehub.grupo2.DB.TopicRepository;
import codehub.grupo2.DB.UserRepository;
import codehub.grupo2.DB.Entity.Post;
import codehub.grupo2.DB.Entity.Topic;
import codehub.grupo2.DB.Entity.UserName;


@Service
public class PostService {
    @Autowired
    private TopicRepository TopicBD;

    @Autowired
    private PostRepository PostBD;

    @Autowired
    private UserRepository UserBD;

    public Post getPost(String title){
        return PostBD.findByTitle(title);
    }

    public String registerPost(UserName user, String title, String text, Topic topic) {
        if (text.length() > 240) {
            return "Numero de caracteres excedido";
        }
        Post existingPost = PostBD.findByTitle(title);
        if (existingPost != null && existingPost.getTopic().equals(topic)) {
            return "El post ya está asociado con este tema.";
        }
        if (topic.getId() == null) {
            TopicBD.save(topic); 
        }
        Post post = new Post(user, title, text, topic);
        PostBD.save(post);
        user.getPosts().add(post);
        UserBD.save(user);
        topic.getPosts().add(post);
        TopicBD.save(topic);

        return title;
    }
    
    

    public List<Post> getAllPost(){
        return PostBD.findAll();
    }

    @Transactional
    public void deletePost(String title) {
        Post post = PostBD.findByTitle(title);
        if (post != null) {
            UserName user = post.getUsername();
            Topic topic = post.getTopic();
    
            user.getPosts().remove(post);
            topic.getPosts().remove(post); 
    
            PostBD.delete(post); 

            UserBD.save(user); 
            TopicBD.save(topic);
        }
    }
    

    public Post getPostById(Long id){
        return PostBD.findById(id).orElse(null);
    }


    public List<Post> getPostByTopic(Topic topic){
        return PostBD.findByTopic(topic);
    }

    public List<Post> findPostsByRegex(String regex) {
            Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
            return PostBD.findAll().stream()
                .filter(post -> pattern.matcher(post.getText()).find() || pattern.matcher(post.getTitle()).find())
                .collect(Collectors.toList());
    }

    public Map<String, Object> getPostWithOwnership(Post post, UserName user) {
        Map<String, Object> data = new HashMap<>();
        data.put("post", post);
        data.put("isOwner", user != null && post.getUsername().getId().equals(user.getId()));
        return data;
    }

    public List<Map<String, Object>> getPostsWithOwnership(List<Post> posts, UserName user) {
        return posts.stream().map(post -> {
            Map<String, Object> data = new HashMap<>();
            data.put("post", post);
            data.put("isOwner", user != null && post.getUsername().getId().equals(user.getId()));
            return data;
        }).collect(Collectors.toList());
    }
    
}