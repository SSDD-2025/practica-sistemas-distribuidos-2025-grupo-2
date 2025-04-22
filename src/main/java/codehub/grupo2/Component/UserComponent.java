package codehub.grupo2.Component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import codehub.grupo2.DB.Entity.UserName;
import codehub.grupo2.Dto.UserNameDTO;

@Component
@SessionScope
public class UserComponent {
    
private UserNameDTO user;

public UserNameDTO getUser() {
    return user;
}

public void setUser(UserNameDTO user) {
    this.user = user;
}

public void logout() {
    this.user = null;   
}

public boolean isLogged() {
    return this.user != null;
}

public boolean isUser(UserName user) {
    return this.user.equals(user);
}

}
