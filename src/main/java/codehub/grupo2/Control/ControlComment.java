package codehub.grupo2.Control;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import codehub.grupo2.Component.PostComponent;
import codehub.grupo2.Component.UserComponent;
import codehub.grupo2.DB.Entity.Post;
import codehub.grupo2.DB.Entity.UserName;
import codehub.grupo2.Service.CommentService;
import codehub.grupo2.Service.PostService;
import codehub.grupo2.Service.UserService;

@Controller
@Component
public class ControlComment {

    @Autowired
    private CommentService CommentService;

    @Autowired
    private UserService UserService;

    @Autowired
    private UserComponent userComponent;

    @Autowired
    private PostService PostService;

    @Autowired
    private PostComponent postComponent;

    @GetMapping("/createComment")
    public String showCreateComment(@RequestParam long id, Model model) {
    model.addAttribute("postId", id);  
    postComponent.setPost(PostService.getPostById(id));
    return "addComment";  
}

    @PostMapping("/createComment")
    public String showCreateComment(@RequestParam String content, Model model) {
        UserName user = userComponent.getUser();
        if (user == null) {
            return "redirect:/home";  
        }
        Post post = postComponent.getPost();
        if(post==null || PostService.getPostById(post.getId()) == null){
            model.addAttribute("error", "Post not found or deleted");
            return "redirect:/post";    
        }
        CommentService.registerComment(user,content, post);
        return "redirect:/showMoreP/" + postComponent.getPost().getId();
    }

    @PostMapping("/deleteComment")
    public String deleteComment(@RequestParam long id, Model model) {
        UserName user = userComponent.getUser();
        if (user == null) {
            return "redirect:/home";
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
