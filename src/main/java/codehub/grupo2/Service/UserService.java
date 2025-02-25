package codehub.grupo2.Service;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import codehub.grupo2.DB.UserRepository;
import codehub.grupo2.DB.Entity.UserName;


@Service
public class UserService {
    @Autowired
    private UserRepository UserBD;

    public UserName getUser(String username){
        return UserBD.findByUsername(username);
    }

    public UserName registerUsername(String name,String password ,String email){

        //meter validación de campos.

        UserName user = new UserName(name,password,email);
        UserBD.save(user);
        return user;
    }

    public List<UserName> getAllUsers(){
        return UserBD.findAll();
    }

    public void deleteUser(String username){
        UserName user = UserBD.findByUsername(username);
        UserBD.delete(user);
    }   


}
