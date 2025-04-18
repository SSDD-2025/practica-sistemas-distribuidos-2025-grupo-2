package codehub.grupo2.Control;

import java.io.IOException;
import java.sql.SQLException;

import javax.sql.rowset.serial.SerialException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import codehub.grupo2.DB.Entity.UserName;
import codehub.grupo2.Security.CustomUserDetails;
import codehub.grupo2.Service.CommentService;
import codehub.grupo2.Service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
@Component
public class ControlUser {
    @Autowired
    private UserService userService;

    @Autowired
    private CommentService commentService;

    @GetMapping("/")
    public String Empty(Model model, HttpServletRequest request) {
        return this.showHome(model, request);
    }
    

    @GetMapping("/register")
    public String ShowRegister(Model model, HttpServletRequest request) {
        CsrfToken token = (CsrfToken) request.getAttribute(CsrfToken.class.getName()); 
        model.addAttribute("csrfToken", token);
        model.addAttribute("check", "");
        return "register";
    } 

    /*@PostMapping("/logOut")
    public String logout(HttpSession session) {
        SecurityContextHolder.clearContext();
        userComponent.logout();
        session.invalidate();
        return "redirect:/home";
    }*/

    @PostMapping("/register")
    public String Register(@RequestParam String username, @RequestParam String password, @RequestParam String email, Model model,HttpServletRequest request) {
        CsrfToken token = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
        model.addAttribute("check", userService.registerUsername(username, password, email));
        model.addAttribute("csrfToken", token);
        return "home";
    }
    
    @GetMapping("/init")
    public String init(Model model, HttpServletRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/home";
        }

        UserName user = userService.getLoggedUser();
        if (user == null) {
            return "redirect:/home";
        }

        CsrfToken token = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
        if (token != null) {
            model.addAttribute("csrfToken", token);
        }

        model.addAttribute("user", user);
        model.addAttribute("error", "");
        return "init";
    }
    @PostMapping("/guest")
    public String guest(Model model, HttpServletRequest request) {
        return "init";
    }
    @GetMapping("/guest")
    public String guestGet(Model model, HttpServletRequest request) {
        return "init";
    }

    @GetMapping("/acc")
    public String GoAccGet(Model model, HttpSession session, HttpServletRequest request) throws SQLException, IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/home";
        }
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        UserName user = userDetails.getUser();
        if (user == null) {
            return "redirect:/home";
        }
        String profilePictureBase64 = userService.convertBlobToBase64(user.getProfilePicture());
        CsrfToken token = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
        model.addAttribute("csrfToken", token);
        model.addAttribute("profilePicture", profilePictureBase64);
        model.addAttribute("user", user);
        model.addAttribute("posts", user.getPosts());
        Boolean showPassword = (Boolean) session.getAttribute("showPassword");
        model.addAttribute("showPassword", showPassword != null ? showPassword : false);
        if (user.getRawPassword() == null) {
            user.setRawPassword("Pass not found");
        }
        return "myProfile";
    }

    @PostMapping("/acc")
    public String GoAccPost(Model model, HttpServletRequest request) throws SQLException, IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/home";
        }
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        UserName user = userDetails.getUser();
        if (user == null) {
            return "redirect:/home";
        }
        String profilePictureBase64 = userService.convertBlobToBase64(user.getProfilePicture());
        CsrfToken token = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
        model.addAttribute("csrfToken", token);
        model.addAttribute("profilePictureBase64", profilePictureBase64);
        model.addAttribute("user", user);
        model.addAttribute("posts", user.getPosts());
        if (user.getRawPassword() == null) {
            user.setRawPassword("Pass not found");
        }
        return "myProfile";
    }
        

    @PostMapping("/showPassword")
    public String showPassword(Model model, HttpSession session, HttpServletRequest request) {
        CsrfToken token = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
        session.setAttribute("showPassword", true);
        model.addAttribute("csrfToken", token);
        return "redirect:/acc";
    }
    
    @PostMapping("/hidePassword")
    public String hidePassword(HttpSession session, HttpServletRequest request, Model model) {
        CsrfToken token = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
        session.setAttribute("showPassword", false);
        model.addAttribute("csrfToken", token);
        return "redirect:/acc";
    }
    

        @PostMapping("/deleteUserConfirmation")
        public String deleteUserConfirmation(HttpServletRequest request,Model model) {
            CsrfToken token = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
            model.addAttribute("csrfToken", token);
            return "deleteUser";
        }

        @PostMapping("/deleteUserDefinitive")
        @Transactional
        public String deleteUserDefinitive(Model model, HttpSession session,HttpServletRequest request ) {
            CsrfToken token = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            UserName user = userDetails.getUser();
            commentService.deleteCommentsByUser(user.getId());
            userService.deleteUser(user.getUsername());
            session.invalidate();
            model.addAttribute("check", "User deleted correctly");
            model.addAttribute("csrfToken", token);
            return "home";
        }
        

        @GetMapping("/editProfile")
        public String showEditProfile(Model model, HttpServletRequest request) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !authentication.isAuthenticated()) {
                return "redirect:/home";
            }
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            UserName user = userDetails.getUser();
            if (user == null) {
                return "redirect:/home";
            }
            CsrfToken token = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
            model.addAttribute("csrfToken", token);
            model.addAttribute("user", user);
            return "editProfile";
        }

        @PostMapping("/updateProfile")
        public String updateProfile(@RequestParam String username, @RequestParam String password, @RequestParam String email, Model model,HttpServletRequest request) {
            CsrfToken token = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            UserName user = userDetails.getUser();
            if (user == null) {
                return "redirect:/home";
            }
            model.addAttribute("profilePictureBase64", user.getProfilePictureBase64());
            model.addAttribute("user", user); 
            model.addAttribute("posts", user.getPosts());
            if(userService.editUser(username, password, email, user.getId()) == 1){
                model.addAttribute("error","Error updating the profile, make sure you followed our rules");
                return "myProfile";
            }
            model.addAttribute("check", "User Uploaded Correctly");
            model.addAttribute("csrfToken", token);
            return "myProfile";
        }

        @GetMapping("/uploadProfilePicture")
        public String getMethodName() {
            return "uploadProfilePicture";
        }

        @PostMapping("/uploadProfilePicture")
        public String uploadProfilePicture(@RequestParam MultipartFile file,Model model,HttpServletRequest request) throws IOException, SerialException, SQLException {
            CsrfToken token = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            UserName user = userDetails.getUser();
            try {
                byte[] bytes = file.getBytes();
                userService.saveProfilePicture(user, bytes);
            } catch (IOException e) {
                return "uploadProfilePicture";
            }
            model.addAttribute("csrfToken", token);
            return "redirect:/acc";
        }

        @GetMapping("/home")
        public String showHome(Model model, HttpServletRequest request) {
            CsrfToken token = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
            if (token != null) {
                model.addAttribute("csrfToken", token);
            }
            model.addAttribute("error", "");
            model.addAttribute("check", "");
            return "home";
        }

}