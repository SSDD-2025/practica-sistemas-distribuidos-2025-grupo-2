package codehub.grupo2;


import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import javax.sql.rowset.serial.SerialException;

import org.springframework.beans.factory.annotation.Autowired;

import codehub.grupo2.Component.*;
import codehub.grupo2.DB.Entity.*;
import codehub.grupo2.Service.*;
import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;







@Controller
@Component
public class Control {


    @Autowired
    private UserService UserService;

    @Autowired
    private PostService PostService;

    @Autowired
    private TopicService TopicService;

    @Autowired
    private CommentService CommentService;

    @Autowired
    private UserComponent userComponent;

    @Autowired
    private PostComponent postComponent;

    //SESSION + PAGES OF SESSION

    @GetMapping("/home")
    public String ShowHome(Model model) {
        model.addAttribute("error", "");
        model.addAttribute("check", "");
        return "home";
    }

    @GetMapping("/")
    public String ShowHome2(Model model) {
        model.addAttribute("error", "");
        model.addAttribute("check", "");
        return "home";
    }

    @GetMapping("/register")
    public String ShowRegister(Model model) {
        model.addAttribute("check", "");
        return "register";
    }

    @PostMapping("/login")
    public String Login(@RequestParam String username, @RequestParam String password, Model model) {
        if(UserService.login(username, password)){
            userComponent.setUser(UserService.getUser(username));
            model.addAttribute("error", "");
            return "redirect:/init";
        }
        else{
            model.addAttribute("error", "Invalid username or password");
            return "home";
        }
    }
    
    @PostMapping("/register")
    public String Register(@RequestParam String username, @RequestParam String password, @RequestParam String email, Model model) {
        model.addAttribute("check", UserService.registerUsername(username, password, email));
        return "home";
    }
    

    @GetMapping("/logOut")
    public String Logout(HttpSession session) {
        userComponent.logout();
        session.invalidate();
        return "redirect:/home";
    }


    //POSTS
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



    //TOPICS
    @GetMapping("/topic")
        public String Topic(Model model) {
            List<Topic> topiclist = TopicService.getAllTopics();
            if(topiclist.isEmpty()){
                model.addAttribute("error", "No topics avaiable");
                return "topic";
            }
            model.addAttribute("topics", topiclist);
            return "topic";
        }
        
        @GetMapping("/addTopic")
        public String showAddTopic(Model model) {
            model.addAttribute("check", "");
            return "addTopic"; 
        }
        
        @PostMapping("/addTopic")
        public String addTopic(@RequestParam String topicName, Model model) {
            TopicService.newTopic(topicName);
            model.addAttribute("check", "Tema Agregado Correctamente");
            return "redirect:/topic";
        }

    @GetMapping("/topic/{id}")
    public String showTopicPost(@PathVariable Long id, Model model) {
        Optional<Topic> topic = TopicService.getTopicById(id);
        if(!topic.isPresent()){
            model.addAttribute("error", "Topic not found");
            return "redirect:/topic"; 
        }
            List<Post> posts = PostService.getPostByTopic(topic.get());
            model.addAttribute("posts", posts);
            model.addAttribute("topicName", topic.get().getTopicName());
            return "postByTopic"; 
    }
    

    @PostMapping("/deleteTopic")
    public String deleteTopic(@RequestParam long id) {
         UserName user = userComponent.getUser();
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
        
        return "redirect:/topic";
        }

    //COMMENTS
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
    
    
    
    //MAIN MENU
        
    @GetMapping("/init")
    public String init( Model model) {
        UserName user = userComponent.getUser();
        if (user == null) {
            return "redirect:/home"; 
        }
        model.addAttribute("user", user); 
        model.addAttribute("error", "");
        return "init";
    }

        //USER PROFILE

        @PostMapping("/acc")
        public String GoAccPost(Model model) throws SQLException, IOException {
            UserName user = userComponent.getUser();
            if (user == null) {
                return "redirect:/home";
            }
            String profilePictureBase64 = UserService.convertBlobToBase64(user.getProfilePicture());
            model.addAttribute("profilePictureBase64", profilePictureBase64);
            model.addAttribute("user", user); 
            model.addAttribute("posts", user.getPosts());
            return "myProfile";
        }

        @GetMapping("/acc")
        public String GoAccGet(Model model, HttpSession session) throws SQLException, IOException {
            UserName user = userComponent.getUser();
            if (user == null) {
                return "redirect:/home";
            }
            String profilePictureBase64 = UserService.convertBlobToBase64(user.getProfilePicture());
            model.addAttribute("profilePicture", profilePictureBase64);
            model.addAttribute("user", user); 
            model.addAttribute("posts", user.getPosts());
            Boolean showPassword = (Boolean) session.getAttribute("showPassword");
            model.addAttribute("showPassword", showPassword != null ? showPassword : false);

            return "myProfile";
        }

        @PostMapping("/showPassword")
        public String showPassword(Model model, HttpSession session) {
            session.setAttribute("showPassword", true);
            return "redirect:/acc"; 
        }
    
        @PostMapping("/hidePassword")
        public String hidePassword(HttpSession session) {
            session.setAttribute("showPassword", false);
            return "redirect:/acc";
        }

        @PostMapping("/deleteUserConfirmation")
        public String deleteUserConfirmation() {
            return "deleteUser";
        }

        @PostMapping("/deleteUserDefinitive")
        @Transactional
        public String deleteUserDefinitive(Model model, HttpSession session) {
            UserName user = userComponent.getUser();
            CommentService.deleteCommentsByUser(user.getId());
            UserService.deleteUser(user.getUsername());
            userComponent.setUser(UserService.getUser(user.getUsername()));
            userComponent.logout();
            session.invalidate();
            model.addAttribute("check", "User deleted correctly");
            return "home";
        }
        

        @GetMapping("/editProfile")
        public String showEditProfile(Model model) {
            UserName user = userComponent.getUser();
            if (user == null) {
                return "redirect:/home";
            }
            model.addAttribute("user", user);
            return "editProfile";
        }

        @PostMapping("/updateProfile")
        public String updateProfile(@RequestParam String username, @RequestParam String password, @RequestParam String email, Model model) {
            UserName user = userComponent.getUser();
            if (user == null) {
                return "redirect:/home";
            }
            model.addAttribute("profilePictureBase64", user.getProfilePictureBase64());
            model.addAttribute("user", user); 
            model.addAttribute("posts", user.getPosts());
            if(UserService.editUser(username, password, email, user.getId()) == 1){
                model.addAttribute("error","Error updating the profile, make sure you followed our rules");
                return "myProfile";
            }
            userComponent.setUser(UserService.getUser(username));
            model.addAttribute("check", "User Uploaded Correctly");
            return "myProfile";
        }

        @GetMapping("/uploadProfilePicture")
        public String getMethodName() {
            return "uploadProfilePicture";
        }

        @PostMapping("/uploadProfilePicture")
        public String uploadProfilePicture(@RequestParam MultipartFile file) throws IOException, SerialException, SQLException {
            UserName user = userComponent.getUser();
            try {
                byte[] bytes = file.getBytes();
                UserService.saveProfilePicture(user, bytes);
                userComponent.setUser(UserService.getUser(user.getUsername()));
            } catch (IOException e) {
                return "uploadProfilePicture";
            }
            return "redirect:/acc";
        }

}
     