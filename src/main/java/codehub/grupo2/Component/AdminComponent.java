package codehub.grupo2.Component;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import codehub.grupo2.DB.Entity.UserName;
import codehub.grupo2.DB.UserRepository;

@Component
public class AdminComponent implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.findByUsername("admin") == null) {
            UserName admin = new UserName();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("password12345"));
            admin.setEmail("admin@localhost.com");
            admin.setProfilePicture(null);
            admin.setPosts(Collections.emptyList());
            admin.setRoles(Collections.singletonList("ADMIN"));
            admin.setRawPassword("password12345");
            userRepository.save(admin);
        }
    }
}
