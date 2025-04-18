package codehub.grupo2.Service;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.Base64;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import javax.sql.rowset.serial.SerialException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import codehub.grupo2.DB.UserRepository;
import codehub.grupo2.DB.Entity.UserName;
import codehub.grupo2.Dto.UserNameDTO;
import codehub.grupo2.Dto.UserNameMapper;

@Service
public class UserService {

    @Autowired
    private UserNameMapper userNameMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository UserBD;

    @Autowired
    @Lazy
    private CommentService commentService;

    public UserName getUser(String username) {
        return UserBD.findByUsername(username);
    }

    public String registerUsername(String name, String password, String email) {
        if (password.length() < 12) {
            return "Password must have at least 12 characters";
        }
        if (UserBD.findByUsername(name) != null) {
            return "Username already exists";
        }
        if (UserBD.findByEmail(email) != null) {
            return "Email already exists";
        }
        if (!email.contains("@")) {
            return "Invalid email";
        }
        
        UserName user = new UserName(name, passwordEncoder.encode(password), email);
        user.setRawPassword(password);
        UserBD.save(user);
        return name;
    }

    public UserName getLoggedUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return UserBD.findByUsername(username);
    }

    public Boolean login(String username, String password) {
        UserName user = UserBD.findByUsername(username);
        if (user == null) {
            return false;
        }
        return passwordEncoder.matches(password, user.getPassword());
    }

    public List<UserName> getAllUsers() {
        return UserBD.findAll();
    }

    @Transactional
    public void deleteUser(String username) {
        UserName user = UserBD.findByUsername(username);
        if (user != null) {
            UserBD.delete(user);
        }
    }

    public void saveProfilePicture(UserName user, byte[] profilePicture) throws IOException, SerialException, SQLException {
        if (profilePicture != null && profilePicture.length > 0) {
            Blob blob = new javax.sql.rowset.serial.SerialBlob(profilePicture);
            user.setProfilePicture(blob);
            UserBD.save(user);
        }
    }

    public int editUser(String username, String password, String email, Long id) {
        Optional<UserName> currentUser = UserBD.findById(id);
        if (currentUser.isEmpty()) {
            return 1;
        }
        UserName user = currentUser.get();
        UserName userN = UserBD.findByUsername(username);
        if (userN != null && !userN.getId().equals(id)) {
            return 1;
        }
        UserName userE = UserBD.findByEmail(email);
        if (userE != null && !userE.getId().equals(id)) {
            return 1;
        }
        if (!email.contains("@")) {
            return 1;
        }
        if (!password.isEmpty()) {
            if (password.length() < 12) {
                return 1;
            }
            user.setPassword(passwordEncoder.encode(password)); 
            user.setRawPassword(password); 
        }
        user.setEmail(email);
        user.setUsername(username);
        UserBD.save(user);
        return 0;
    }

    public String convertBlobToBase64(Blob blob) throws SQLException, IOException {
        if (blob == null) {
            return null;
        }
        try (InputStream inputStream = blob.getBinaryStream()) {
            byte[] bytes = inputStream.readAllBytes();
            return Base64.getEncoder().encodeToString(bytes);
        }
    }

    public UserNameDTO getLoggedUserDTO() {
        UserName user = getLoggedUser();
        if (user != null) {
            return userNameMapper.toDTO(user);
        }
        return null;
    }

    public Collection<UserNameDTO> getAllUsersDTO() {
        List<UserName> users = UserBD.findAll();
        return userNameMapper.toDTOs(users);
    }

    public UserNameDTO getUserByIdDTO(long id) {
        Optional<UserName> user = UserBD.findById(id);
        if (user.isPresent()) {
            return userNameMapper.toDTO(user.get());
        }
        return null;
    }
}