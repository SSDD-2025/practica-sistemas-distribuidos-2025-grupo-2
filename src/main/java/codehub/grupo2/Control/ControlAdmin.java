package codehub.grupo2.Control;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import codehub.grupo2.Dto.CommentDTO;
import codehub.grupo2.Dto.PostDTO;
import codehub.grupo2.Service.*;
import jakarta.servlet.http.HttpServletRequest;

@Controller
public class ControlAdmin {

    @Autowired
    private CommentService commentService;

    @Autowired
    private UserService userService;

    @Autowired
    private TopicService topicService;

    @Autowired
    private PostService postService;

@GetMapping("/adminPanel")
    public String showAdminPanel(Model model, HttpServletRequest request) {

        List<CommentDTO> comments = (List<CommentDTO>) commentService.getAllCommentsDTO();


        List<Map<String, Object>> enrichedComments = comments.stream().map(comment -> {
            Map<String, Object> commentData = new HashMap<>();
            commentData.put("id", comment.id());
            commentData.put("text", comment.text());
            commentData.put("date", comment.date());
            commentData.put("user", comment.user());
            PostDTO post = postService.getPostByIdDTO(comment.postId());
            commentData.put("post", post != null ? post : new PostDTO(null, "Post Not Found", "", null, null, null));
            return commentData;
        }).collect(Collectors.toList());

        // Agregar al modelo
        model.addAttribute("comments", enrichedComments);
        model.addAttribute("users", userService.getAllUsersDTO());
        model.addAttribute("posts", postService.getAllPostDTO()); 
        model.addAttribute("topics", topicService.getAllTopicsDTO()); 
        CsrfToken token = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
        model.addAttribute("csrfToken", token);

        return "adminPanel"; 
    }
    @PostMapping("/admin/deleteComment")
    public String deleteComment(@RequestParam("comment") Long commentId, HttpServletRequest request, Model model) {
        commentService.deleteComment(commentId);
        return "redirect:/adminPanel";
    }

    @PostMapping("/admin/deleteUser")
    public String deleteUser(@RequestParam("user") String username) {
        userService.deleteUserDTO(username);
        return "redirect:/adminPanel";
    }

    @PostMapping("/admin/deletePost")
    public String deletePost(@RequestParam("post") String title) {
        postService.deletePostDTO(title);
        return "redirect:/adminPanel";
    }

    @PostMapping("/admin/deleteTopic")
    public String deleteTopic(@RequestParam("topic") Long id) {
        topicService.deleteTopicDTO(id);
        return "redirect:/adminPanel";
    }
}