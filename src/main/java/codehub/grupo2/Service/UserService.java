package codehub.grupo2.Service;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.Base64;
import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import javax.sql.rowset.serial.SerialException;

import org.hibernate.engine.jdbc.BlobProxy;
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

    public UserNameDTO getUser(String username) {
        UserName user = UserBD.findByUsername(username);
        return user != null ? userNameMapper.toDTO(user) : null;
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

    public UserNameDTO getLoggedUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        UserName user = UserBD.findByUsername(username);
        return user != null ? userNameMapper.toDTO(user) : null;
    }

    public Boolean login(String response, String loginRequest) {
        UserName user = UserBD.findByUsername(response);
        if (user == null) {
            return false;
        }
        return passwordEncoder.matches(loginRequest, user.getPassword());
    }

    @Transactional
    public void deleteUser(String username) {
        UserName user = UserBD.findByUsername(username);
        if (user != null) {
            UserBD.delete(user);
        }
    }

    public void saveProfilePicture(Long userId, byte[] profilePicture) throws IOException, SerialException, SQLException {
        UserName user = UserBD.findById(userId)
            .orElseThrow(() -> new NoSuchElementException("User not found"));
        if (profilePicture != null && profilePicture.length > 0) {
            Blob blob = new javax.sql.rowset.serial.SerialBlob(profilePicture);
            user.setProfilePicture(blob);
            UserBD.save(user);
        }
    }

    public int editUser(UserNameDTO updatedUserDTO, Long id) {
        Optional<UserName> currentUser = UserBD.findById(id);
        if (currentUser.isEmpty()) {
            return 1;
        }
        UserName user = currentUser.get();
        UserName userN = UserBD.findByUsername(updatedUserDTO.username());
        if (userN != null && !userN.getId().equals(id)) {
            return 1;
        }
        UserName userE = UserBD.findByEmail(updatedUserDTO.email());
        if (userE != null && !userE.getId().equals(id)) {
            return 1;
        }
        if (!updatedUserDTO.email().contains("@")) {
            return 1;
        }
        if (updatedUserDTO.password() != null && !updatedUserDTO.password().isEmpty()) {
            if (updatedUserDTO.password().length() < 12) {
                return 1;
            }
            user.setPassword(passwordEncoder.encode(updatedUserDTO.password()));
        }
        user.setEmail(updatedUserDTO.email());
        user.setUsername(updatedUserDTO.username());
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


    public UserNameDTO getUserByIdDTO(long id) {
        Optional<UserName> user = UserBD.findById(id);
        if (user.isPresent()) {
            return userNameMapper.toDTO(user.get());
        }
        return null;
    }

    public void deleteProfilePicture(Long userId) throws SQLException {
        UserName user = UserBD.findById(userId)
            .orElseThrow(() -> new NoSuchElementException("User not found"));
        user.setProfilePicture(null);
        UserBD.save(user);
    }

    public void createUserImage(long id, URI location, InputStream inputStream, long size) {
        UserName user = UserBD.findById(id).orElseThrow();
        user.setProfilePicture(BlobProxy.generateProxy(inputStream, size));
        UserBD.save(user);
    }

    @Transactional
    public void deleteUserDTO(String username) {
        UserName user = UserBD.findByUsername(username);
        if (user != null) {
            commentService.deleteCommentsByUser(user.getId());
            UserBD.delete(user);
        }
    }

    public Blob getUserImage(long id) {
        Optional<UserName> userOptional = UserBD.findById(id);
        if (userOptional.isEmpty()) {
            return null;
        }
        UserName user = userOptional.get();
        return user.getProfilePicture();
    }

    /*public UserNameDTO replaceUserImage(long id, UserNameDTO updatedUserDTO) throws SQLException {
        UserName oldUser = UserBD.findById(id).orElseThrow();
        UserName updatedUser = userNameMapper.toDomain(updatedUserDTO);
        updatedUser.setId(id);
        if (oldUser.getProfilePicture() != null) {
        updatedUser.setProfilePicture(BlobProxy.generateProxy(
        oldUser.getProfilePicture().getBinaryStream(),
        oldUser.getProfilePicture().length()));
        updatedUser.setProfilePicture(oldUser.getProfilePicture());
        }
        UserBD.save(updatedUser);
        return userNameMapper.toDTO(updatedUser);
    }*/

    public void replaceUserImage(long id, InputStream inputStream, long size) {
        UserName user = UserBD.findById(id).orElseThrow();
        if(user.getProfilePicture() == null){
        throw new NoSuchElementException();
        }
        user.setProfilePicture(BlobProxy.generateProxy(inputStream, size));
        UserBD.save(user);
    }


    public void deleteUserImage(long id) {
        UserName user = UserBD.findById(id).orElseThrow();
        if(user.getProfilePicture() == null){
        throw new NoSuchElementException();
        }
        user.setProfilePicture(null);
        UserBD.save(user);
    }

    public Collection<UserNameDTO> getAllUsersDTO() {
        List<UserName> users = UserBD.findAll();
        return userNameMapper.toDTOs(users);
    }


}