package codehub.grupo2;

import org.springframework.beans.factory.annotation.Autowired;
import codehub.grupo2.DB.*;
import codehub.grupo2.DB.Entity.User;

import org.springframework.stereotype.Controller;
import org.springframework.boot.CommandLineRunner;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class Control implements CommandLineRunner{
    @Autowired
    private UserRepository UserBD;
    @Override
    public void run(String... args) throws Exception {
        UserBD.save(new User("CazaMopis43", "Ensaimadas", "Ucendos"));
        UserBD.save(new User("Sonaca", "Enanos", "Sonacas"));
    }   

    @PostMapping("/login")
    public String Login(@RequestParam String username, @RequestParam String password){
        User user = UserBD.findByUsername(username);
        if(user == null){
            return "User not found";
        }
        if(user.getPassword().equals(password)){
            return "Inico";
        }
        return "Wrong password";
    }
    
}
