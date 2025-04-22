package codehub.grupo2.Control;

import java.util.Collection;
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

import codehub.grupo2.Dto.PostDTO;
import codehub.grupo2.Dto.TopicDTO;
import codehub.grupo2.Dto.UserNameDTO;
import codehub.grupo2.Security.CustomUserDetails;
import codehub.grupo2.Service.PostService;
import codehub.grupo2.Service.TopicService;
import jakarta.servlet.http.HttpServletRequest;

@Controller
@Component
public class ControlTopic {

    @Autowired
    private TopicService TopicService;

    @Autowired
    private PostService PostService;

    @GetMapping("/topic")
    public String Topic(Model model, HttpServletRequest request) {
        Collection<TopicDTO> topiclist = TopicService.getAllTopicsDTO();
        Boolean isLogged = false;
        UserNameDTO user = null;
    
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && 
            !"anonymousUser".equals(authentication.getPrincipal())) {
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            user = userDetails.getUserNameDTO();
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
        TopicService.newTopicDTO(topicName);
        model.addAttribute("check", "Topic added");
        model.addAttribute("csrfToken", token);
        return "redirect:/topic";
    }

    @GetMapping("/topic/{id}")
    public String showTopicPost(@PathVariable Long id, Model model, HttpServletRequest request) {
        Optional<TopicDTO> topic = TopicService.getTopicByIdDTO(id);
        Boolean isLogged = false;
        UserNameDTO user = null;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated() && 
            !"anonymousUser".equals(authentication.getPrincipal())) {
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            user = userDetails.getUserNameDTO();
            isLogged = true;
        }

        if (!topic.isPresent()) {
            model.addAttribute("error", "Topic not found");
            return "redirect:/topic"; 
        }

        List<PostDTO> posts = PostService.getPostByTopicDTO(topic.get());
        CsrfToken token = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
        model.addAttribute("isLogged", isLogged);
        model.addAttribute("csrfToken", token);
        model.addAttribute("posts", posts);
        model.addAttribute("topicName", topic.get().topicName());
        return "postByTopic"; 
    }
    
    @PostMapping("/deleteTopic")
    public String deleteTopic(@RequestParam long id, HttpServletRequest request, Model model) {
        CsrfToken token = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        UserNameDTO user = userDetails.getUserNameDTO();
        if (user == null) {
            return "redirect:/home";
        }
        Optional<TopicDTO> topicOpt = TopicService.getTopicByIdDTO(id);
        if (!topicOpt.isPresent()) {
            return "redirect:/topic"; 
        }
        
        TopicDTO topic = topicOpt.get();
        List<PostDTO> posts = PostService.getPostByTopicDTO(topic);
        for (PostDTO p : posts) {
            PostService.deletePostDTO(p.title()); 
        }
        TopicService.deleteTopicDTO(id);
        model.addAttribute("csrfToken", token);
        return "redirect:/topic";
    }
}