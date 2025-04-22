package codehub.grupo2.Component;

import java.time.LocalDate;
import java.util.Collections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import codehub.grupo2.DB.Entity.UserName;
import codehub.grupo2.DB.Entity.Topic;
import codehub.grupo2.DB.Entity.Post;
import codehub.grupo2.DB.UserRepository;
import codehub.grupo2.DB.TopicRepository;
import codehub.grupo2.DB.PostRepository;

@Component
public class UserInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TopicRepository topicRepository;

    @Autowired
    private PostRepository postRepository;

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

        if (userRepository.findByUsername("Tester") == null) {
            UserName tester = new UserName();
            tester.setUsername("Tester");
            tester.setPassword(passwordEncoder.encode("123451234512345"));
            tester.setEmail("tester@localhost.com");
            tester.setProfilePicture(null);
            tester.setRoles(Collections.singletonList("USER"));
            tester.setRawPassword("123451234512345");
            userRepository.save(tester);
        }

        // Crear 10 topics
        String[] topicNames = {
            "Java", "Python", "Web Development", "Database", "Cloud Computing",
            "Machine Learning", "Cybersecurity", "Mobile Apps", "DevOps", "AI"
        };
        for (String topicName : topicNames) {
            if (topicRepository.findByTopicName(topicName) == null) {
                Topic topic = new Topic(topicName);
                topicRepository.save(topic);
            }
        }

        String[][] posts = {
            {"Introducción a Java", "Explorando las bases de Java.", "Java", "Sonaca"},
            {"Python para principiantes", "Cómo empezar con Python.", "Python", "Prueba"},
            {"Creando una web con HTML", "Guía básica de HTML y CSS.", "Web Development", "Tester"},
            {"Optimizando bases de datos", "Consejos para mejorar el rendimiento.", "Database", "Sonaca"},
            {"AWS vs Azure", "Comparativa de servicios en la nube.", "Cloud Computing", "Prueba"},
            {"Machine Learning en Python", "Usando scikit-learn para ML.", "Machine Learning", "Tester"},
            {"Seguridad en aplicaciones", "Cómo proteger tus apps.", "Cybersecurity", "Sonaca"},
            {"Desarrollo de apps Android", "Primeros pasos con Android Studio.", "Mobile Apps", "Prueba"},
            {"CI/CD con Jenkins", "Automatizando despliegues.", "DevOps", "Tester"},
            {"Introducción a la IA", "Conceptos básicos de inteligencia artificial.", "AI", "Sonaca"},
            {"Java avanzado", "Programación funcional en Java.", "Java", "Prueba"},
            {"APIs con Flask", "Creando APIs REST con Python.", "Python", "Tester"},
            {"Diseño responsivo", "Técnicas para web adaptable.", "Web Development", "Sonaca"},
            {"SQL vs NoSQL", "Diferencias y casos de uso.", "Database", "Prueba"},
            {"Deep Learning con TensorFlow", "Redes neuronales profundas.", "Machine Learning", "Tester"}
        };

        for (String[] postData : posts) {
            String title = postData[0];
            String text = postData[1];
            String topicName = postData[2];
            String username = postData[3];

            if (postRepository.findByTitle(title) == null) {
                Topic topic = topicRepository.findByTopicName(topicName);
                UserName user = userRepository.findByUsername(username);
                if (topic != null && user != null) {
                    Post post = new Post(user, title, text, topic);
                    postRepository.save(post);
                }
            }
        }
    }
}