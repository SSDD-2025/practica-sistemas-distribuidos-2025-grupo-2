package codehub.grupo2.Component;

import java.util.Collections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import codehub.grupo2.DB.Entity.UserName;
import codehub.grupo2.DB.UserRepository;

@Component
public class UserInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Value("${admin.username}")
    private String adminUsername;

    @Value("${admin.password}")
    private String adminPassword;

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.findByUsername("admin") == null) {
            UserName admin = new UserName();
            admin.setUsername("admin");
            admin.setPassword(adminPassword);
            admin.setEmail("admin@localhost.com");
            admin.setProfilePicture(null);
            admin.setRoles(Collections.singletonList("ADMIN"));
            admin.setRawPassword(null);
            userRepository.save(admin);
        } 

        if (userRepository.findByUsername("Sonaca") == null) {
            UserName sonaca = new UserName();
            sonaca.setUsername("Sonaca");
            sonaca.setPassword(passwordEncoder.encode("123451234512345"));
            sonaca.setEmail("sonaca@localhost.com");
            sonaca.setProfilePicture(null);
            sonaca.setRoles(Collections.singletonList("USER"));
            sonaca.setRawPassword("123451234512345");
            userRepository.save(sonaca);
        }

        if (userRepository.findByUsername("Prueba") == null) {
            UserName prueba = new UserName();
            prueba.setUsername("Prueba");
            prueba.setPassword(passwordEncoder.encode("123451234512345"));
            prueba.setEmail("prueba@localhost.com");
            prueba.setProfilePicture(null);
            prueba.setRoles(Collections.singletonList("USER"));
            prueba.setRawPassword("123451234512345");
            userRepository.save(prueba);
        }
    }
}