package codehub.grupo2.Component;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import codehub.grupo2.DB.Entity.UserName;

@Component
@SessionScope
public class UserComponent {
    
private UserName user;

public UserName getUser() {
    return user;
}

public void setUser(UserName user) {
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
