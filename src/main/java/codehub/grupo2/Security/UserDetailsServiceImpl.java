package codehub.grupo2.Security;

import codehub.grupo2.DB.UserRepository;
import codehub.grupo2.DB.Entity.UserName;
import codehub.grupo2.Dto.UserNameDTO;
import codehub.grupo2.Dto.UserNameMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

@Primary
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserNameMapper userNameMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserName user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found: " + username);
        }

        UserNameDTO userDTO = userNameMapper.toDTO(user);
        return new CustomUserDetails(userDTO);
    }
}