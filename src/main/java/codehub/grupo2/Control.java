package codehub.grupo2;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import codehub.grupo2.DB.*;
import codehub.grupo2.DB.Entity.*;
import codehub.grupo2.Service.UserService;
import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.boot.CommandLineRunner;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.GetMapping;






@Controller
@Component
public class Control implements CommandLineRunner {
    @Autowired
    private UserService UserService;

    @Autowired
    private PostRepository PostBD;
    
    @Autowired
    private TopicRepository TopicBD;

    @Override
    public void run(String... args) throws Exception {
               
        UserService.registerUsername("Sonaca", "Sonaca", "Sonaca");
        UserService.registerUsername("Admin", "Admin", "Admin");
        UserService.registerUsername("User", "User", "User");

        Post post1 = new Post(UserService.getUser("Sonaca"), "Post1", "Post1");
        Post post2 = new Post(UserService.getUser("Sonaca"), "Post2", "Post2");
        Post post3 = new Post(UserService.getUser("Sonaca"), "Post3", "Post3");
        Post post4 = new Post(UserService.getUser("Sonaca"), "Post4", "Post4");
        Post post5 = new Post(UserService.getUser("Sonaca"), "Post5", "Post5");    
        Post post6 = new Post(UserService.getUser("Sonaca"), "Post6", "Post6");
        Post post7 = new Post(UserService.getUser("Sonaca"), "Post7", "Post7");
        Post post8 = new Post(UserService.getUser("Sonaca"), "Post8", "Post8");
        Post post9 = new Post(UserService.getUser("Sonaca"), "Post9", "Post9");
        Post post10 = new Post(UserService.getUser("Sonaca"), "Post10", "Post10");

        Topic topic1 = new Topic("Python");
        Topic topic2 = new Topic("Pascal");
        Topic topic3 = new Topic("Java");
        TopicBD.save(topic1);
        TopicBD.save(topic2);
        TopicBD.save(topic3);


        PostBD.save(post1);
        PostBD.save(post2);
        PostBD.save(post3);
        PostBD.save(post4);
        PostBD.save(post5);
        PostBD.save(post6);
        PostBD.save(post7);
        PostBD.save(post8);
        PostBD.save(post9);
        PostBD.save(post10);
    }


    //SESSION + PAGES OF SESSION

    @GetMapping("/home")
    public String ShowHome(Model model) {
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
        model.addAttribute("error", "Contraseña incorrecta.");
        return "home";
    }
    
    @PostMapping("/register")
    public String Register(@RequestParam String username, @RequestParam String password, @RequestParam String email, Model model) {
        UserService.registerUsername(username, password, email);
        model.addAttribute("check", "Usuario Registrado Correctamente");
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
        List<Post> postlist = PostBD.findAll();
        model.addAttribute("posts", postlist);
        return "post";
    }

    @PostMapping("/showMorePost")
    public String showMorePostPost(@RequestParam("id") long id, Model model) {
        Post post = PostBD.findById(id).get();
        if (post != null) {
            model.addAttribute("post", post);
            return "showMorePost";
        } else {
           //Programar error
            return "redirect:/post";
        }
    }

    @GetMapping("/showMorePost")
    public String showMorePostGet(@RequestParam("id") long id, Model model) {
        Post post = PostBD.findById(id).get();
        if (post != null) {
            model.addAttribute("post", post);
            return "showMorePost";
        } else {
        //Programar error
            return "redirect:/post";
        }
    }

    
    


    //TOPICS

    @GetMapping("/topic")
        public String Topic(Model model) {
            List<Topic> topiclist = TopicBD.findAll();
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
            TopicBD.save(new Topic(topicName));
            model.addAttribute("check", "Tema Agregado Correctamente");
            return "addTopic";
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


        


}
