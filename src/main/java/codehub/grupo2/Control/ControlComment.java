package codehub.grupo2.Control;

import java.time.LocalDate;

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
import codehub.grupo2.Dto.CommentDTO;
import codehub.grupo2.Dto.PostDTO;
import codehub.grupo2.Dto.UserNameDTO;
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
        PostDTO post = PostService.getPostByIdDTO(id);
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
        UserNameDTO user = userDetails.getUserNameDTO(); 
        if (user == null) {
            return "redirect:/home";  
        }
        PostDTO post = postComponent.getPost();
        if (post == null || PostService.getPostByIdDTO(post.id()) == null) {
            model.addAttribute("error", "Post not found or deleted");
            return "redirect:/post";    
        }
        CommentDTO commentDTO = new CommentDTO(null, LocalDate.now(), content, user, post);
        CommentService.registerCommentDTO(commentDTO);
        return "redirect:/showMoreP/" + post.id();
    }

    @PostMapping("/deleteComment")
    public String deleteComment(@RequestParam long id, Model model, HttpServletRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        UserNameDTO user = userDetails.getUserNameDTO();
        if (user == null) {
            return "redirect:/home";
        }

        CommentDTO comment = CommentService.getCommentByIdDTO(id);
        if (comment == null || !comment.user().id().equals(user.id())) {
            model.addAttribute("error", "You can't delete this comment");
            return "redirect:/showMoreP/" + (comment != null ? comment.post().id() : "post");
        }

        int comp = CommentService.deleteComment(id);
        if (comp == 0) {
            return "redirect:/showMoreP/" + postComponent.getPost().id();
        } else {
            model.addAttribute("error", "Comment not found or deleted");
            return "redirect:/init";
        }
    }
}