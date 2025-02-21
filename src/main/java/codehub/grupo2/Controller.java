package codehub.grupo2;

import org.springframework.beans.factory.annotation.Autowired;
import codehub.grupo2.DB.UserRepository;
import org.springframework.stereotype.Controller;

@Controller
public class Controller {
    @Autowired
    private UserRepository repository;

}
