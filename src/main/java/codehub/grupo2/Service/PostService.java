package codehub.grupo2.Service;


import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import codehub.grupo2.DB.PostRepository;
import codehub.grupo2.DB.Entity.Post;
import codehub.grupo2.DB.Entity.Topic;
import codehub.grupo2.DB.Entity.UserName;

@Service
public class PostService {
    @Autowired
    private PostRepository PostBD;

    public Post getPost(String title){
        return PostBD.findByTitle(title);
    }

    public String registerPost(UserName user,String title,String text, Topic topic ){
        if(text.length()>240){
            return "Numero de caracteres excedido";
        }
        Post post = new Post(user,title,text,topic);
        PostBD.save(post);
        user.getPosts().add(post);
        topic.getPosts().add(post);
        return title;
    }

    public List<Post> getAllPost(){
        return PostBD.findAll();
    }

    public void deletePost(String title){
        Post post = PostBD.findByTitle(title);
        PostBD.delete(post);
    }   

    public Post getPostById(Long id){
        return PostBD.findById(id).get();
    }

    public Post editPost(Long id, Optional<String> title, Optional<String> text){
        Post p = getPostById(id);
        if(title.isPresent()){
            p.setTitle(title.get());
        }
        if(text.isPresent()){
            p.setText(text.get());
        }
        return p;
    }
    
}
