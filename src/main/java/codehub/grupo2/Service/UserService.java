package codehub.grupo2.Service;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.hibernate.engine.jdbc.BlobProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import codehub.grupo2.DB.UserRepository;
import codehub.grupo2.DB.Entity.UserName;


@Service
public class UserService {
    @Autowired
    private UserRepository UserBD;

    public UserName getUser(String username){
        return UserBD.findByUsername(username);
    }

    public String registerUsername(String name,String password ,String email){
        if(password.length()<12){
            return "Password must have at least 12 characters";
        }
        if(UserBD.findByUsername(name) != null){
            return "Username already exists";
        }
        if(UserBD.findByEmail(email) != null){
            return "Email already exists";
        }
        if(email.contains("@") == false){
            return "Invalid email";
        }   
        UserName user = new UserName(name,password,email);
        UserBD.save(user);
        return name;
    }

    public Boolean login(String username, String password){
        UserName user = UserBD.findByUsername(username);
        if(user == null){
            return false;
        }
        if(user.getPassword().equals(password)){
            return true;
        }
        return false;
    }

    public List<UserName> getAllUsers(){
        return UserBD.findAll();
    }

    public void deleteUser(String username){
        UserName user = UserBD.findByUsername(username);
        UserBD.delete(user);
    }   

    public void addComment(UserName user){
        UserBD.save(user);
    }

    public void save(UserName user, MultipartFile profilePicture) throws IOException {
        if(!profilePicture.isEmpty()){
            user.setProfilePicture(BlobProxy.generateProxy(profilePicture.getInputStream(), profilePicture.getSize()));
        }
        UserBD.save(user);
    }

    public int editUser(String username, String password, String email, Long id){
        Optional<UserName> currentUser = UserBD.findById(id);
        if(password.length()<12){
            return 1;
        }
        UserName userN = UserBD.findByUsername(username);
        if(userN != null && userN.getId() != id){
            return 1;
        }
        UserName userE = UserBD.findByEmail(email);
        if(userE != null && userE.getId() != id){
            return 1;
        }
        if(email.contains("@") == false){
            return 1;
        }
        if(currentUser.isPresent()){
            currentUser.get().setPassword(password);
            currentUser.get().setEmail(email);
            currentUser.get().setUsername(username);
            UserBD.save(currentUser.get());
            return 0;
        }
        else{
            return 1;
        }
    }


}
