package codehub.grupo2.Security;

import java.util.ArrayList;
import java.util.List;

import codehub.grupo2.DB.UserRepository;
import codehub.grupo2.DB.Entity.UserName;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

@Primary
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public CustomUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    UserName user = userRepository.findByUsername(username);

    if (user == null) {
        throw new UsernameNotFoundException("User not found");
    }
    
    List<GrantedAuthority> roles = new ArrayList<>();
		for (String role : user.getRoles()) {
			roles.add(new SimpleGrantedAuthority("ROLE_" + role));
		}
    
    return new CustomUserDetails(user);
}



}
