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

import codehub.grupo2.Dto.UserNameDTO;
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

    @PostMapping("/register")
    public String Register(@RequestParam String username, @RequestParam String password, @RequestParam String email, Model model, HttpServletRequest request) {
        CsrfToken token = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
        UserNameDTO userDTO = new UserNameDTO(null, username, email, password, null);
        userService.registerUsername(userDTO.username(), userDTO.password(), userDTO.email());
        model.addAttribute("check", userDTO);
        model.addAttribute("csrfToken", token);
        return "home";
    }
    
    @GetMapping("/init")
    public String init(Model model, HttpServletRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/home";
        }

        UserNameDTO user = userService.getLoggedUser();
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
        CsrfToken token = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
        model.addAttribute("csrfToken", token);
        return "init";
    }
    @GetMapping("/guest")
    public String guestGet(Model model, HttpServletRequest request) {
        CsrfToken token = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
        model.addAttribute("csrfToken", token);
        return "init";
    }

    @GetMapping("/acc")
public String GoAccGet(Model model, HttpSession session, HttpServletRequest request) throws SQLException, IOException {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication == null || !authentication.isAuthenticated()) {
        return "redirect:/home";
    }
    CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
    UserNameDTO user = userDetails.getUserNameDTO();
    if (user == null) {
        return "redirect:/home";
    }
    String profilePictureBase64 = userService.convertBlobToBase64(userService.getUserImage(user.id()));
    CsrfToken token = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
    model.addAttribute("csrfToken", token);
    model.addAttribute("profilePicture", profilePictureBase64 != null ? profilePictureBase64 : "default-profile-picture.png");
    model.addAttribute("user", user);
    model.addAttribute("posts", userService.getUserPosts(user.id())); 
    Boolean showPassword = (Boolean) session.getAttribute("showPassword");
    model.addAttribute("showPassword", showPassword != null ? showPassword : false);
    model.addAttribute("rawPassword", "Pass not available");
    return "myProfile";
}

@PostMapping("/acc")
public String GoAccPost(Model model, HttpServletRequest request) throws SQLException, IOException {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication == null || !authentication.isAuthenticated()) {
        return "redirect:/home";
    }
    CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
    UserNameDTO user = userDetails.getUserNameDTO();
    if (user == null) {
        return "redirect:/home";
    }
    String profilePictureBase64 = userService.convertBlobToBase64(userService.getUserImage(user.id()));
    CsrfToken token = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
    model.addAttribute("csrfToken", token);
    model.addAttribute("profilePicture", profilePictureBase64 != null ? profilePictureBase64 : "default-profile-picture.png");
    model.addAttribute("user", user);
    model.addAttribute("posts", userService.getUserPosts(user.id())); 
    model.addAttribute("rawPassword", "Pass not available");
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
    public String deleteUserConfirmation(HttpServletRequest request, Model model) {
        CsrfToken token = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
        model.addAttribute("csrfToken", token);
        return "deleteUser";
    }

    @PostMapping("/deleteUserDefinitive")
    @Transactional
    public String deleteUserDefinitive(Model model, HttpSession session, HttpServletRequest request) {
        CsrfToken token = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        UserNameDTO user = userDetails.getUserNameDTO();
        commentService.deleteCommentsByUser(user.id());
        userService.deleteUserDTO(user.username());
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
        UserNameDTO user = userDetails.getUserNameDTO();
        if (user == null) {
            return "redirect:/home";
        }
        CsrfToken token = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
        model.addAttribute("csrfToken", token);
        model.addAttribute("user", user);
        return "editProfile";
    }

    @PostMapping("/updateProfile")
    public String updateProfile(@RequestParam String username, @RequestParam String password, @RequestParam String email, Model model, HttpServletRequest request) throws SQLException, IOException {
        CsrfToken token = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        UserNameDTO user = userDetails.getUserNameDTO();
        
        if (user == null) {
            return "redirect:/home";
        }

        UserNameDTO updatedUserDTO = new UserNameDTO(user.id(), username, password, email, null);
        if (userService.editUser(updatedUserDTO, user.id()) == 1) {
            model.addAttribute("error", "Error updating the profile, make sure you followed our rules");
            return "myProfile";
        }

        UserNameDTO updatedUser = userService.getUser(username);
        CustomUserDetails newDetails = new CustomUserDetails(updatedUser);
        Authentication newAuth = new org.springframework.security.authentication.UsernamePasswordAuthenticationToken(
            newDetails, authentication.getCredentials(), newDetails.getAuthorities()
        );
        SecurityContextHolder.getContext().setAuthentication(newAuth);

        model.addAttribute("check", "User Uploaded Correctly");
        model.addAttribute("csrfToken", token);
        model.addAttribute("user", updatedUser); 
        model.addAttribute("posts", userService.getUserPosts(updatedUser.id()));
        model.addAttribute("profilePicture", userService.convertBlobToBase64(userService.getUserImage(updatedUser.id())));
        
        return "myProfile";
    }

    @GetMapping("/uploadProfilePicture")
    public String getMethodName(Model model, HttpServletRequest request) {
        CsrfToken token = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
        model.addAttribute("csrfToken", token);
        return "uploadProfilePicture";
    }

    @PostMapping("/deleteProfilePicture")
    public String deleteProfilePicture(Model model, HttpServletRequest request) {
        CsrfToken token = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
        model.addAttribute("csrfToken", token);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/home";
        }
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        UserNameDTO user = userDetails.getUserNameDTO();
        try {
            userService.deleteProfilePicture(user.id());
        } catch (SQLException e) {
            model.addAttribute("error", "Error deleting profile picture: " + e.getMessage());
            return "uploadProfilePicture";
        }
        return "redirect:/acc";
    }

    @PostMapping("/uploadProfilePicture")
    public String uploadProfilePicture(@RequestParam MultipartFile file, Model model, HttpServletRequest request) throws IOException, SerialException, SQLException {
        CsrfToken token = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        model.addAttribute("csrfToken", token);
        UserNameDTO user = userDetails.getUserNameDTO();
        try {
            byte[] bytes = file.getBytes();
            userService.saveProfilePicture(user.id(), bytes);
        } catch (IOException e) {
            return "uploadProfilePicture";
        }
        return "redirect:/acc";
    }

    @GetMapping("/home")
    public String showHome(Model model, HttpServletRequest request) {
        CsrfToken token = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
        if (token != null) {
            model.addAttribute("csrfToken", token);
        }
        return "home";
    }

    @GetMapping("/accessDenied")
    public String showAccessDenied(Model model, HttpServletRequest request) {
        CsrfToken token = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
        if (token != null) {
            model.addAttribute("csrfToken", token);
        }
        return "accessDenied";
    }
}