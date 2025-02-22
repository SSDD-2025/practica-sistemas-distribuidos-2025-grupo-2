package codehub.grupo2;

import org.springframework.beans.factory.annotation.Autowired;
import codehub.grupo2.DB.*;
import codehub.grupo2.DB.Entity.UserName;
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

    @Override
    public void run(String... args) throws Exception {
        UserBD.save(new UserName("CazaMopis43", "Ensaimadas", "Ucendos"));
        UserBD.save(new UserName("Sonaca", "Sonaca", "Sonacas"));
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
            return "redirect:/init"; 
        }
        model.addAttribute("error", "Contraseña incorrecta.");
        return "home";
    }

    @GetMapping("/init")
    public String init(HttpSession session, Model model) {
        UserName user = (UserName) session.getAttribute("user");
        if (user == null) {
            return "redirect:/home"; 
        }
        model.addAttribute("user", user); 
        return "init";
    }
    
    @PostMapping("/register")
    public String Register(@RequestParam String username, @RequestParam String password, @RequestParam String email, Model model) {
        UserBD.save(new UserName(username, password, email));
       
        model.addAttribute("check", "Usuario Registrado Correctamente");
        return "home";
    }
}
