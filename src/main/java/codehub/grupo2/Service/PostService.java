package codehub.grupo2.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import codehub.grupo2.DB.PostRepository;
import codehub.grupo2.DB.TopicRepository;
import codehub.grupo2.DB.Entity.Post;
import codehub.grupo2.DB.Entity.Topic;
import codehub.grupo2.DB.Entity.UserName;

@Service
public class PostService {
    @Autowired
    private TopicRepository TopicBD;

    @Autowired
    private PostRepository PostBD;

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
        topic.getPosts().add(post);
    
        return title;
    }
    
    

    public List<Post> getAllPost(){
        return PostBD.findAll();
    }

    @Transactional
    public void deletePost(String title){
        Post post = PostBD.findByTitle(title);
        if (post != null) {
            post.getUsername().getPosts().remove(post);
            post.getTopic().getPosts().remove(post);
            PostBD.delete(post);
        }
    }

    public Post getPostById(Long id){
        return PostBD.findById(id).orElse(null);
    }

    public Post editPost(Long id, Optional<String> title, Optional<String> text){
        Post p = getPostById(id);
        if (p != null) {
            title.ifPresent(p::setTitle);
            text.ifPresent(p::setText);
            PostBD.save(p);
        }
        return p;
    }

    public List<Post> getPostByTopic(Topic topic){
        return PostBD.findByTopic(topic);
    }
}