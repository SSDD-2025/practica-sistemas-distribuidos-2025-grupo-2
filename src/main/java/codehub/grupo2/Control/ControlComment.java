package codehub.grupo2.Control;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import codehub.grupo2.Component.PostComponent;
import codehub.grupo2.DB.Entity.Comment;
import codehub.grupo2.DB.Entity.Post;
import codehub.grupo2.DB.Entity.UserName;
import codehub.grupo2.Security.CustomUserDetails;
import codehub.grupo2.Service.CommentService;
import codehub.grupo2.Service.PostService;
import jakarta.servlet.http.HttpServletRequest;

@Controller
@Component
public class ControlComment {

    @Autowired
    private CommentService CommentService;

    @Autowired
    private PostService PostService;

    @Autowired
    private PostComponent postComponent;

    @GetMapping("/createComment")
    public String showCreateComment(@RequestParam long id, Model model, HttpServletRequest request) {
        Post post = PostService.getPostById(id);
        if (post == null) {
            model.addAttribute("error", "Post not found");
            return "redirect:/post";
        }
        CsrfToken token = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
        model.addAttribute("csrfToken", token);
        model.addAttribute("postId", id);
        postComponent.setPost(post);
        return "addComment";  
    }

    @PostMapping("/createComment")
    public String createComment(@RequestParam String content, Model model, HttpServletRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        UserName user = userDetails.getUser();
        if (user == null) {
            return "redirect:/home";  
        }
        Post post = postComponent.getPost();
        if (post == null || PostService.getPostById(post.getId()) == null) {
            model.addAttribute("error", "Post not found or deleted");
            return "redirect:/post";    
        }
        CommentService.registerComment(user, content, post);
        return "redirect:/showMoreP/" + post.getId();
    }

    @PostMapping("/deleteComment")
    public String deleteComment(@RequestParam long id, Model model, HttpServletRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        UserName user = userDetails.getUser();

        if (user == null) {
            return "redirect:/home";
        }

        Comment comment = CommentService.getCommentById(id);
        if (!comment.getUsername().getId().equals(user.getId())) {
            model.addAttribute("error", "You cant delete this comment");
            return "redirect:/showMoreP/" + comment.getPost().getId();
        }

        int comp = CommentService.deleteComment(id);
        if (comp == 0) {
            return "redirect:/showMoreP/" + postComponent.getPost().getId();
        } else {
            model.addAttribute("error", "Comment not found or deleted");
            return "redirect:/init";
        }
    }
}