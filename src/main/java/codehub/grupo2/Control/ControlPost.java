package codehub.grupo2.Control;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.csrf.CsrfToken;
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
import codehub.grupo2.Security.CustomUserDetails;
import codehub.grupo2.Service.PostService;
import codehub.grupo2.Service.TopicService;
import codehub.grupo2.Service.UserService;
import jakarta.servlet.http.HttpServletRequest;

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
    public String Post(Model model, HttpServletRequest request) {
        List<Post> postlist = PostService.getAllPost();
        CsrfToken token = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        UserName user = userDetails.getUser();

        List<Map<String, Object>> postData = postlist.stream().map(post -> {
        Map<String, Object> data = new HashMap<>();
        data.put("post", post);
        data.put("isOwner", user != null && post.getUsername().getId().equals(user.getId()));
        return data;
        }).collect(Collectors.toList());
        
        model.addAttribute("csrfToken", token);
        model.addAttribute("posts", postData);
        if (postlist.isEmpty()) {
            model.addAttribute("error", "No posts avaiable");
        }
        return "post";
    }


    @PostMapping("/showMoreP/{id}")
public String showMorePostPost(@PathVariable("id") long id, Model model, HttpServletRequest request) {
    Post post = PostService.getPostById(id);
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
    UserName user = userDetails.getUser();

    if (post != null) {
        postComponent.setPost(post);
        CsrfToken token = (CsrfToken) request.getAttribute(CsrfToken.class.getName());

        Map<String, Object> postData = new HashMap<>();
        postData.put("post", post);
        postData.put("isOwner", user != null && post.getUsername().getId().equals(user.getId()));


        List<Map<String, Object>> commentData = post.getComments().stream().map(comment -> {
            Map<String, Object> data = new HashMap<>();
            data.put("comment", comment);
            data.put("isOwner", user != null && comment.getUsername().getId().equals(user.getId()));
            return data;
        }).collect(Collectors.toList());

        model.addAttribute("csrfToken", token);
        model.addAttribute("posts", postData);
        model.addAttribute("comments", commentData);
        return "showMorePost";
    } else {
        model.addAttribute("error", "No post found");   
        return "redirect:/post";
    }
}

@GetMapping("/showMoreP/{id}")
public String showMorePostGet(@PathVariable("id") long id, Model model, HttpServletRequest request) {
    Post post = PostService.getPostById(id);
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
    UserName user = userDetails.getUser();

    if (post != null) {
        postComponent.setPost(post);
        CsrfToken token = (CsrfToken) request.getAttribute(CsrfToken.class.getName());

    
        Map<String, Object> postData = new HashMap<>();
        postData.put("post", post);
        postData.put("isOwner", user != null && post.getUsername().getId().equals(user.getId()));

      
        List<Map<String, Object>> commentData = post.getComments().stream().map(comment -> {
            Map<String, Object> data = new HashMap<>();
            data.put("comment", comment);
            data.put("isOwner", user != null && comment.getUsername().getId().equals(user.getId()));
            return data;
        }).collect(Collectors.toList());

        model.addAttribute("csrfToken", token);
        model.addAttribute("posts", postData);
        model.addAttribute("comments", commentData);
        return "showMorePost"; 
    } else {
        model.addAttribute("error", "Post Not Found");
        return "redirect:/post";
    }
}
    
    

    @GetMapping("/addPost")
    public String showAddPost(Model model, HttpServletRequest request) {
        List<Topic> topics = TopicService.getAllTopics();
        CsrfToken token = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
        model.addAttribute("csrfToken", token);
        model.addAttribute("topics", topics);
        model.addAttribute("check", "");
        return "addPost"; 
    }

    @PostMapping("/addPost")
    public String showwaddPost(@RequestParam String title, @RequestParam String content, @RequestParam long tid, Model model, HttpServletRequest request) {
        CsrfToken token = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        UserName user = userDetails.getUser();
        if (user == null) {
            return "redirect:/home"; 
        }
        Topic topic = TopicService.getTopicById(tid).get();
        PostService.registerPost(user, title, content, topic);
        model.addAttribute("error", "");
        model.addAttribute("csrfToken", token);
        return "redirect:/post"; 
    }
    
    
    @PostMapping("/deletePost")
    public String deletePost(@RequestParam long id,Model model, HttpServletRequest request) {
        CsrfToken token = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        UserName user = userDetails.getUser();

        if (user == null) {
            return "redirect:/home";
        }

        Post post = PostService.getPostById(id);
        if (post == null) {
            model.addAttribute("error", "Post not found");
            return "redirect:/post";
        }

        if (!post.getUsername().getId().equals(user.getId())) {
            model.addAttribute("error", "You cant delete this post");
            return "redirect:/post";
        }

        PostService.deletePost(PostService.getPostById(id).getTitle());
        userComponent.setUser(UserService.getUser(user.getUsername()));
        model.addAttribute("error", "");
        model.addAttribute("csrfToken", token);
        return "redirect:/post";
    }

}
