package codehub.grupo2.Control;

import java.io.IOException;
import java.sql.SQLException;

import javax.sql.rowset.serial.SerialException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import codehub.grupo2.Component.UserComponent;
import codehub.grupo2.DB.Entity.UserName;
import codehub.grupo2.Service.CommentService;
import codehub.grupo2.Service.UserService;
import jakarta.servlet.http.HttpSession;

@Controller
@Component
public class ControlUser {
    @Autowired
    private UserService UserService;

    @Autowired
    private UserComponent userComponent;

    @Autowired
    private CommentService CommentService;

    
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

    @GetMapping("/logOut")
    public String Logout(HttpSession session) {
        userComponent.logout();
        session.invalidate();
        return "redirect:/home";
    }

    @PostMapping("/register")
    public String Register(@RequestParam String username, @RequestParam String password, @RequestParam String email, Model model) {
        model.addAttribute("check", UserService.registerUsername(username, password, email));
        return "home";
    }

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
