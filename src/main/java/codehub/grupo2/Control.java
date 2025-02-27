package codehub.grupo2;


import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import codehub.grupo2.DB.Entity.*;
import codehub.grupo2.Service.*;
import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.boot.CommandLineRunner;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;






@Controller
@Component
public class Control implements CommandLineRunner {
    @Autowired
    private UserService UserService;

    @Autowired
    private PostService PostService;

    @Autowired
    private TopicService TopicService;

    @Autowired
    private CommentService CommentService;
    

    @Override
    public void run(String... args) throws Exception {
        Topic topic1 = TopicService.newTopic("Python");
        Topic topic2 = TopicService.newTopic("Pascal");
        Topic topic3 = TopicService.newTopic("Java");
    
        UserService.registerUsername("Sonaca", "Sonaca", "Sonaca");
        UserService.registerUsername("Admin", "Admin", "Admin");
        UserService.registerUsername("User", "User", "User");

        UserName user = UserService.getUser("Sonaca");
    

        Post post1 = new Post(user, "Post1", "Post1", topic2);
        Post post2 = new Post(user, "Post2", "Post2", topic1);
        Post post3 = new Post(user, "Post3", "Post3", topic1);
        Post post4 = new Post(user, "Post4", "Post4", topic1);
        Post post5 = new Post(user, "Post5", "Post5", topic1);
    

        PostService.registerPost(user, post1.getTitle(), post1.getText(), post1.getTopic());
        PostService.registerPost(user, post2.getTitle(), post2.getText(), post2.getTopic());
        PostService.registerPost(user, post3.getTitle(), post3.getText(), post3.getTopic());
        PostService.registerPost(user, post4.getTitle(), post4.getText(), post4.getTopic());
        PostService.registerPost(user, post5.getTitle(), post5.getText(), post5.getTopic());
        
    }


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
    public String Login(@RequestParam String username, @RequestParam String password, HttpSession session, Model model) {
        UserName sessionUser = UserService.getUser(username);
        if (sessionUser == null) {
            model.addAttribute("error", "Usuario no encontrado.");
            return "home";
        }
        if (sessionUser.getPassword().equals(password)) {
            session.setAttribute("user", sessionUser); 
            return "redirect:/init"; 
        }
        model.addAttribute("error", "Contrasena incorrecta.");
        return "home";
    }
    
    @PostMapping("/register")
    public String Register(@RequestParam String username, @RequestParam String password, @RequestParam String email, Model model) {
        UserService.registerUsername(username, password, email);
        model.addAttribute("check", UserService.registerUsername(username, password, email));
        return "home";
    }
    

    @GetMapping("/logOut")
    public String Logout(HttpSession session) {
        session.removeAttribute("user");
        session.invalidate();
        return "redirect:/home";
    }


    //POSTS
    @GetMapping("/post")
    public String Post(Model model) {
        List<Post> postlist = PostService.getAllPost();
        model.addAttribute("posts", postlist);
        return "post";
    }
    @PostMapping("/showMoreP/{id}")
    public String showMorePostPost(@PathVariable("id") long id, Model model) {
        Post post = PostService.getPostById(id);
        if (post != null) {
            model.addAttribute("posts", post);
            return "showMorePost";
        } else {
            return "redirect:/post";
        }
    }
    

    @GetMapping("/showMoreP/{id}")
    public String showMorePostGet(@PathVariable("id") long id, Model model) {
        Post post = PostService.getPostById(id);
        if (post != null) {
            model.addAttribute("posts", post);  
            return "showMorePost"; 
        } else {
            return "redirect:/post";
        }
    }
    
    


     @GetMapping("/addPost")
    public String showAddPost(Model model) {
        model.addAttribute("check", "");
        return "addPost"; 
    }

    @PostMapping("/addPost")
    public String addPost(@RequestParam String title, @RequestParam String content, HttpSession session, Model model, @RequestParam Topic topic) {
        UserName user = (UserName) session.getAttribute("user");
        if (user == null) {
            return "redirect:/home";
        }
        PostService.registerPost(user, title, content,topic);
        model.addAttribute("check", "Post Agregado Correctamente");
        return "addPost";
    }

    
    @PostMapping("/deletePost")
    public String deletePost(@RequestParam long id, HttpSession session) {
        UserName user = (UserName) session.getAttribute("user");
        if (user == null) {
            return "redirect:/home";
        }
        PostService.deletePost(PostService.getPostById(id).getTitle());
        return "redirect:/init";
    }

    @GetMapping("/editPost")
    public String showEditPost(@RequestParam long id, HttpSession session, Model model) {
        UserName user = (UserName) session.getAttribute("user");
        if (user == null) {
            return "redirect:/home";
        }
        Post post = PostService.getPostById(id);
        model.addAttribute("post", post);
        return "editPost";        
    }
    

    @PostMapping("/editPost")
    public String editPost(@RequestParam long id, HttpSession session, Model model,@RequestParam Optional<String> title,@RequestParam Optional<String> text){
        UserName user = (UserName) session.getAttribute("user");
        if (user == null) {
            return "redirect:/home";
        }
        PostService.editPost(id,title,text);
        return "redirect:/acc";
    }
    


    //TOPICS

    @GetMapping("/topic")
        public String Topic(Model model) {
            List<Topic> topiclist = TopicService.getAllTopics();
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
            return "addTopic";
        }

    @GetMapping("/topic/{id}")
    public String showTopicPost(@PathVariable Long id, Model model) {
        Optional<Topic> topic = TopicService.getTopicById(id);
        if (topic.isPresent()) {
            List<Post> posts = PostService.getPostByTopic(topic.get());
            model.addAttribute("posts", posts);
            model.addAttribute("topicName", topic.get().getTopicName());
            return "postByTopic"; 
        } else {
            return "redirect:/topic"; 
        }
    }

    @PostMapping("/deleteTopic")
    public String deleteTopic(@RequestParam long id, HttpSession session) {
         UserName user = (UserName) session.getAttribute("user");
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
    @GetMapping("createComment")
    public String showCreateComment(@RequestParam long id, Model model) {
        model.addAttribute("postId", id);  
        return "addComment";  
    }
    
    @PostMapping("createComment")
    public String showCreateComment(@RequestParam long id, @RequestParam String content, HttpSession session, Model model) {
        UserName user = (UserName) session.getAttribute("user");
        if (user == null) {
            return "redirect:/home";  
        }
        Post post = PostService.getPostById(id);
        CommentService.registerComment(user, content, content, post);
        model.addAttribute("check", "Comentario Agregado Correctamente");
        return "addComment";
    }
    

    

    @PostMapping("deleteComment")
    public String deleteComment(@RequestParam long id, HttpSession session) {
        UserName user = (UserName) session.getAttribute("user");
        if (user == null) {
            return "redirect:/home";
        }
        CommentService.deleteComment(String.valueOf(id));
        return "redirect:/acc";
    }

    @GetMapping("editComment")
    public String showEditComment(@RequestParam long id, HttpSession session, Model model) {
        UserName user = (UserName) session.getAttribute("user");
        if (user == null) {
            return "redirect:/home";
        }
        Comment comment = CommentService.getCommentById(id);
        model.addAttribute("comment", comment);
        return "editComment";        
    }
    
    @PostMapping("editComment")
    public String editComment(@RequestParam long id, HttpSession session, Model model,@RequestParam String text){
        UserName user = (UserName) session.getAttribute("user");
        if (user == null) {
            return "redirect:/home";
        }
        CommentService.editComment(id,text);
        return "redirect:/acc";
    }
        
    
    //MAIN MENU
        
    @GetMapping("/init")
    public String init(HttpSession session, Model model) {
        UserName user = (UserName) session.getAttribute("user");
        if (user == null) {
            return "redirect:/home"; 
        }
        model.addAttribute("user", user); 
        return "init";
    }

        //USER PROFILE

        @PostMapping("/acc")
        public String GoAccPost(Model model, HttpSession session) {
            UserName user = (UserName) session.getAttribute("user");
            if (user == null) {
                return "redirect:/home";
            }
            model.addAttribute("user", user); 
            model.addAttribute("posts", user.getPosts());
            return "myProfile";
        }

        @GetMapping("/acc")
        public String GoAccGet(Model model, HttpSession session) {
            UserName user = (UserName) session.getAttribute("user");
            if (user == null) {
                return "redirect:/home";
            }
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
        public String deleteUserDefinitive(HttpSession session, Model model) {
            UserName user = (UserName) session.getAttribute("user");
            UserService.deleteUser(user.getUsername());
            session.removeAttribute("user");
            session.invalidate();
            model.addAttribute("check", "Usuario Eliminado Correctamente");
            return "home";
        }
}
