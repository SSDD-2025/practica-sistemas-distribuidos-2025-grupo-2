package codehub.grupo2.Control;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
        model.addAttribute("users", userService.getAllUsers());
        model.addAttribute("topics", topicService.getAllTopics());
        model.addAttribute("posts", postService.getAllPost());
        model.addAttribute("comments", commentService.getAllComments());

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
        userService.deleteUser(username);
        return "redirect:/adminPanel";
    }

    @PostMapping("/admin/deletePost")
    public String deletePost(@RequestParam("post") String title) {
        postService.deletePost(title);
        return "redirect:/adminPanel";
    }

    @PostMapping("/admin/deleteTopic")
    public String deleteTopic(@RequestParam("topic") Long id) {
        topicService.deleteTopic(id);
        return "redirect:/adminPanel";
    }
}