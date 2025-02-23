package codehub.grupo2;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import codehub.grupo2.DB.*;
import codehub.grupo2.DB.Entity.*;
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
    private UserRepository UserBD;

    @Autowired
    private PostRepository PostBD;
    
    @Autowired
    private TopicRepository TopicBD;

    @Override
    public void run(String... args) throws Exception {
        UserName Sonaca = new UserName("Sonaca", "Sonaca", "Sonaca");
        UserName Admin = new UserName("Admin", "Admin", "Admin");
        UserName User = new UserName("User", "User", "User");
        UserBD.save(Sonaca);
        UserBD.save(Admin);
        UserBD.save(User);
        Post post1 = new Post(Sonaca, "Post1", "Post1");
        Post post2 = new Post(Sonaca, "Post2", "Post2");
        Post post3 = new Post(Sonaca, "Post3", "Post3");
        Topic topic1 = new Topic("Python");
        Topic topic2 = new Topic("Pascal");
        Topic topic3 = new Topic("Java");
        TopicBD.save(topic1);
        TopicBD.save(topic2);
        TopicBD.save(topic3);


        PostBD.save(post1);
        PostBD.save(post2);
        PostBD.save(post3);
    }

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
        UserName sessionUser = UserBD.findByUsername(username);
        if (sessionUser == null) {
            model.addAttribute("error", "Usuario no encontrado.");
            return "home";
        }
        if (sessionUser.getPassword().equals(password)) {
            session.setAttribute("user", sessionUser); 
            return "redirect:/init2"; 
        }
        model.addAttribute("error", "Contraseña incorrecta.");
        return "home";
    }

    @GetMapping("/init2")
    public String init(HttpSession session, Model model) {
        UserName user = (UserName) session.getAttribute("user");
        if (user == null) {
            return "redirect:/home"; 
        }
        model.addAttribute("user", user); 
        return "init2";
    }
    
    @PostMapping("/register")
    public String Register(@RequestParam String username, @RequestParam String password, @RequestParam String email, Model model) {
        UserBD.save(new UserName(username, password, email));
       
        model.addAttribute("check", "Usuario Registrado Correctamente");
        return "home";
    }

    @GetMapping("/logOut")
    public String Logout(HttpSession session) {
        session.removeAttribute("user");
        session.invalidate();
        return "redirect:/home";
    }

    @PostMapping("/acc")
public String GoAcc(Model model, HttpSession session) {
    UserName user = (UserName) session.getAttribute("user");
    if (user == null) {
        return "redirect:/home";
    }
    model.addAttribute("user", user); 
    model.addAttribute("posts", user.getPosts());
    return "myProfile";
}
    @GetMapping("/topic")
        public String Topic(Model model) {
            List<Topic> topiclist = TopicBD.findAll();
            model.addAttribute("topics", topiclist);
            return "topic";
        }
        
}
