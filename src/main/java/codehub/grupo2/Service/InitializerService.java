package codehub.grupo2.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;

import javax.sql.rowset.serial.SerialException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import codehub.grupo2.DB.Entity.Post;
import codehub.grupo2.DB.Entity.Topic;
import codehub.grupo2.DB.Entity.UserName;

@Service
public class InitializerService {

    @Autowired
    private UserService userService;

    @Autowired
    private  PostService postService;

    @Autowired
    private  CommentService commentService;

    @Autowired
    private  TopicService topicService;

    public void initialize() throws IOException, SerialException, SQLException {
        userService.registerUsername("Sonaca","12345678901234567890" , "sonaca@gmail.com");
        userService.registerUsername("ASastre03","12345678901234567890" , "ASastre03@gmail.com");
        userService.registerUsername("CazaMopis43","12345678901234567890" , "CazaMopis43@gmail.com");
        userService.registerUsername("User","ContraseñaUsuario" , "user@gmail.com");

        UserName Sonaca = userService.getUser("Sonaca");
        UserName ASastre03 = userService.getUser("ASastre03"); 
        UserName CazaMopis43 = userService.getUser("CazaMopis43");
        UserName User = userService.getUser("User");

        Topic Java = topicService.newTopic("Java");
        Topic Python =topicService.newTopic("Python");
        Topic SQL =topicService.newTopic("SQL");

        postService.registerPost(Sonaca, "Java2", "Java is a programming language", Java);
        postService.registerPost(CazaMopis43, "SQL", "SQL is a language for working with databases", SQL);
        postService.registerPost(ASastre03, "Python", "Python is a programming language", Python);
        postService.registerPost(User, "Java", "Java is a programming language", Java);

        Post JavaPost = postService.getPost("Java");
        Post SQLPost = postService.getPost("SQL");
        Post PythonPost = postService.getPost("Python");


        commentService.registerComment(Sonaca,"Java is a programming language", JavaPost);
        commentService.registerComment(CazaMopis43, "SQL is a language for working with databases", SQLPost);
        commentService.registerComment(ASastre03,"Python is a programming language", PythonPost);  

        String imagePath = "READMEDATA\\ProfileImages\\1729106333526609.png";

        byte[] imageBytes = Files.readAllBytes(Paths.get(imagePath));

        userService.saveProfilePicture(Sonaca, imageBytes);

        imagePath = "READMEDATA\\ProfileImages\\1729106333526609.png";

        imageBytes = Files.readAllBytes(Paths.get(imagePath));

        userService.saveProfilePicture(User, imageBytes);

    }

}
