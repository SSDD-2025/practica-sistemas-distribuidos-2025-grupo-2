package codehub.grupo2.Control;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import codehub.grupo2.Component.PostComponent;
import codehub.grupo2.Component.UserComponent;
import codehub.grupo2.DB.Entity.Post;
import codehub.grupo2.DB.Entity.Topic;
import codehub.grupo2.DB.Entity.UserName;
import codehub.grupo2.Service.PostService;
import codehub.grupo2.Service.TopicService;
import codehub.grupo2.Service.UserService;

@Controller
@Component
public class ControlPost {

    @Autowired
    private PostService PostService;

    @Autowired
    private PostComponent postComponent;

    @Autowired
    private UserService UserService;

    @Autowired
    private UserComponent userComponent;

    @Autowired
    private TopicService TopicService;

    @GetMapping("/post")
    public String Post(Model model) {
        List<Post> postlist = PostService.getAllPost();
        model.addAttribute("posts", postlist);
        if(postlist.isEmpty()==true){
            model.addAttribute("error", "No posts avaiable");
            return "post";
        }
        
        return "post";
    }


    @PostMapping("/showMoreP/{id}")
    public String showMorePostPost(@PathVariable("id") long id, Model model) {
        Post post = PostService.getPostById(id);
        if (post != null) {
            postComponent.setPost(post);
            model.addAttribute("posts", post);
            return "showMorePost";
        } else {
            model.addAttribute("error", "No post found");   
            return "redirect:/post";
        }
    }
    

    @GetMapping("/showMoreP/{id}")
    public String showMorePostGet(@PathVariable("id") long id, Model model) {
        Post post = PostService.getPostById(id);
        if (post != null) {
            postComponent.setPost(post);
            model.addAttribute("posts", post);  
            return "showMorePost"; 
        } else {
            model.addAttribute("error", "Post Not Found");
            return "redirect:/post";
        }
    }
    
    

    @GetMapping("/addPost")
    public String showAddPost(Model model) {
        List<Topic> topics = TopicService.getAllTopics();
        model.addAttribute("topics", topics);
        model.addAttribute("check", "");
        return "addPost"; 
    }

    @PostMapping("/addPost")
    public String showwaddPost(@RequestParam String title,  @RequestParam String content,@RequestParam long tid,Model model) {
        UserName user = userComponent.getUser();
        if (user == null) {
            return "redirect:/home"; 
        }
        Topic topic = TopicService.getTopicById(tid).get();
        PostService.registerPost(user, title, content, topic);
        model.addAttribute("error", "");
        return "redirect:/post"; 
    }
    
    
    @PostMapping("/deletePost")
    public String deletePost(@RequestParam long id,Model model) {
        UserName user = userComponent.getUser();
        if (user == null) {
            return "redirect:/home";
        }
        PostService.deletePost(PostService.getPostById(id).getTitle());
        userComponent.setUser(UserService.getUser(user.getUsername()));
        model.addAttribute("error", "");
        return "redirect:/post";
    }

}
