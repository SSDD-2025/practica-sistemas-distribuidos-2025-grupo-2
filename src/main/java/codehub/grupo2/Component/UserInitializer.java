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

        // Crear usuarios (sin cambios)
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
            UserName test = new UserName();
            test.setUsername("UserTest");
            test.setPassword(passwordEncoder.encode("123451234512345"));
            test.setEmail("usertest@localhost.com");
            test.setProfilePicture(null);
            test.setRoles(Collections.singletonList("USER"));
            test.setRawPassword("123451234512345");
            userRepository.save(test);
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

        // Crear 10 topics en inglés
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

        // Crear 15 posts en inglés
        String[][] posts = {
            {"Introduction to Java", "Exploring the basics of Java programming.", "Java", "Sonaca"},
            {"Python for Beginners", "How to get started with Python.", "Python", "UserTest"},
            {"Building a Website with HTML", "A beginner's guide to HTML and CSS.", "Web Development", "Tester"},
            {"Optimizing Databases", "Tips for improving database performance.", "Database", "Sonaca"},
            {"AWS vs Azure", "Comparing cloud services.", "Cloud Computing", "UserTest"},
            {"Machine Learning with Python", "Using scikit-learn for machine learning.", "Machine Learning", "Tester"},
            {"Application Security", "How to secure your applications.", "Cybersecurity", "Sonaca"},
            {"Developing Android Apps", "Getting started with Android Studio.", "Mobile Apps", "UserTest"},
            {"CI/CD with Jenkins", "Automating deployments with Jenkins.", "DevOps", "Tester"},
            {"Introduction to AI", "Basic concepts of artificial intelligence.", "AI", "Sonaca"},
            {"Advanced Java", "Functional programming in Java.", "Java", "UserTest"},
            {"APIs with Flask", "Building REST APIs with Python Flask.", "Python", "Tester"},
            {"Responsive Web Design", "Techniques for adaptive web design.", "Web Development", "Sonaca"},
            {"SQL vs NoSQL", "Differences and use cases.", "Database", "UserTest"},
            {"Deep Learning with TensorFlow", "Building deep neural networks.", "Machine Learning", "Tester"}
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