package codehub.grupo2;


import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import codehub.grupo2.Component.*;
import codehub.grupo2.DB.Entity.*;
import codehub.grupo2.Service.*;
import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.boot.CommandLineRunner;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
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

    @Autowired
    private UserComponent userComponent;

    @Autowired
    private PostComponent postComponent;
    

    @Override
    public void run(String... args) throws Exception {
        Topic topic1 = TopicService.newTopic("Python");
        Topic topic2 = TopicService.newTopic("Pascal");
        Topic topic3 = TopicService.newTopic("Java");
    
        UserService.registerUsername("Sonaca", "12345678901234567890", "sonaca@gmail.com");
        UserService.registerUsername("Admin", "Admin", "Admin");
        UserService.registerUsername("User", "User", "User");

        UserName user = UserService.getUser("Sonaca");
    

        Post post1 = new Post(user, "Post1", "Post1", topic2);
        Post post2 = new Post(user, "Post2", "Post2", topic1);
        Post post3 = new Post(user, "Post3", "Post3", topic1);
        Post post4 = new Post(user, "Post4", "Post4", topic3);
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
        return "redirect:/post"; 
    }
    
    
    @PostMapping("/deletePost")
    public String deletePost(@RequestParam long id) {
        UserName user = userComponent.getUser();
        if (user == null) {
            return "redirect:/home";
        }
        PostService.deletePost(PostService.getPostById(id).getTitle());
        return "redirect:/post";
    }

    //TOPICS

    @GetMapping("/topic")
        public String Topic(Model model) {
            List<Topic> topiclist = TopicService.getAllTopics();
            if(topiclist.isEmpty()){
                model.addAttribute("error", "No topics avadible");
                return "redirect:/topic";
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
        CommentService.registerComment(user, content, content, post);
        model.addAttribute("check" , "Comment added correctly"); 
        return "redirect:/post"; 
    }

    @PostMapping("/deleteComment")
    public String deleteComment(@RequestParam long id) {
        UserName user = userComponent.getUser();
        if (user == null) {
            return "redirect:/home";
        }
        CommentService.deleteComment(String.valueOf(id));
        return "redirect:/acc";
    }

    @GetMapping("/editComment")
    public String showEditComment(@RequestParam long id,  Model model) {
        UserName user = userComponent.getUser();
        if (user == null) {
            return "redirect:/home";
        }
        Comment comment = CommentService.getCommentById(id);
        model.addAttribute("comment", comment);
        return "editComment";        
    }
    
    @PostMapping("/editComment")
    public String editComment(@RequestParam long id, Model model,@RequestParam String text){
        UserName user = userComponent.getUser();
        if (user == null) {
            return "redirect:/home";
        }
        CommentService.editComment(id,text);
        return "redirect:/acc";
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
        public String GoAccPost(Model model) {
            UserName user = userComponent.getUser();
            if (user == null) {
                return "redirect:/home";
            }
            model.addAttribute("user", user); 
            model.addAttribute("posts", user.getPosts());
            return "myProfile";
        }

        @GetMapping("/acc")
        public String GoAccGet(Model model, HttpSession session) {
            UserName user = userComponent.getUser();
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
        public String deleteUserDefinitive( Model model,HttpSession session) {
            UserName user = userComponent.getUser();
            UserService.deleteUser(user.getUsername());
            userComponent.logout();
            session.invalidate();
            model.addAttribute("check", "User deleted correctly");
            return "home";
        }

        @PostMapping("/profilePicture/new")
	        public String newPost(Model model, Post post, MultipartFile image) throws Exception {
            UserName user = userComponent.getUser();
            UserService.save(user, image);
		    return "saved_post";
	    }

}
     