package codehub.grupo2.DB.Entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;

@Entity
public class UserName {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String username;
    private String password;
    private String email;


    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "user")
    private List<Post> posts;

    protected UserName() {}

    public UserName(String username, String password, String email){
        this.username = username;
        this.password = password;
        this.email = email;
        this.posts = new ArrayList<>();
    }

    public String getUsername(){
        return this.username;
    }

    public String getPassword(){
        return this.password;
    }

    public String getEmail(){
        return this.email;
    }

    public List<Post> getPosts(){
        return this.posts;
    }

}
