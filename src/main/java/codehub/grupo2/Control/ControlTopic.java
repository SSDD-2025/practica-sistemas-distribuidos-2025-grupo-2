package codehub.grupo2.Control;

import java.util.List;
import java.util.Optional;

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

import codehub.grupo2.DB.Entity.Post;
import codehub.grupo2.DB.Entity.UserName;
import codehub.grupo2.Security.CustomUserDetails;
import codehub.grupo2.Service.PostService;
import codehub.grupo2.Service.TopicService;
import jakarta.servlet.http.HttpServletRequest;
import codehub.grupo2.DB.Entity.Topic;

@Controller
@Component
public class ControlTopic {

    @Autowired
    private TopicService TopicService;

    @Autowired
    private PostService PostService;

    @GetMapping("/topic")
    public String Topic(Model model, HttpServletRequest request) {
        List<Topic> topiclist = TopicService.getAllTopics();
        Boolean isLogged = false;
        UserName user = null;
    
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && 
            !"anonymousUser".equals(authentication.getPrincipal())) {
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            user = userDetails.getUser();
            isLogged = true;
        }
    
        if (topiclist.isEmpty()) {
            model.addAttribute("error", "No topics available");
        }
    
        CsrfToken token = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
        model.addAttribute("isLogged", isLogged);
        model.addAttribute("csrfToken", token);
        model.addAttribute("topics", topiclist);
        return "topic";
    }
        
    @GetMapping("/addTopic")
    public String showAddTopic(Model model, HttpServletRequest request) {
        CsrfToken token = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
        model.addAttribute("csrfToken", token);
        model.addAttribute("check", "");
        return "addTopic"; 
    }
        
    @PostMapping("/addTopic")
    public String addTopic(@RequestParam String topicName, Model model, HttpServletRequest request) {
        CsrfToken token = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
        TopicService.newTopic(topicName);
        model.addAttribute("check", "Topic added");
        model.addAttribute("csrfToken", token);
        return "redirect:/topic";
    }

@GetMapping("/topic/{id}")
public String showTopicPost(@PathVariable Long id, Model model, HttpServletRequest request) {
    Optional<Topic> topic = TopicService.getTopicById(id);
    Boolean isLogged = false;
    UserName user = null;

    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication != null && authentication.isAuthenticated() && 
        !"anonymousUser".equals(authentication.getPrincipal())) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        user = userDetails.getUser();
        isLogged = true;
    }

    if (!topic.isPresent()) {
        model.addAttribute("error", "Topic not found");
        return "redirect:/topic"; 
    }

    List<Post> posts = PostService.getPostByTopic(topic.get());
    CsrfToken token = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
    model.addAttribute("isLogged", isLogged);
    model.addAttribute("csrfToken", token);
    model.addAttribute("posts", posts);
    model.addAttribute("topicName", topic.get().getTopicName());
    return "postByTopic"; 
}
    
    @PostMapping("/deleteTopic")
    public String deleteTopic(@RequestParam long id, HttpServletRequest request, Model model) {
        CsrfToken token = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        UserName user = userDetails.getUser();
        if (user == null) {
            return "redirect:/home";
        }
        Optional<Topic> topicOpt = TopicService.getTopicById(id);
        if (!topicOpt.isPresent()) {
            return "redirect:/topic"; 
        }
        
        Topic topic = topicOpt.get();
        List<Post> posts = PostService.getPostByTopic(topic);
        for (Post p : posts) {
            PostService.deletePost(p.getTitle()); 
        }
        TopicService.deleteTopic(id);
        model.addAttribute("csrfToken", token);
        return "redirect:/topic";
    }
}