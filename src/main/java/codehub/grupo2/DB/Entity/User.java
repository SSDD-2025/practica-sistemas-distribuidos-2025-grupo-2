package codehub.grupo2.DB.Entity;

import java.util.HashMap;

import jakarta.persistence.*;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String username;
    private String password;
    private String email;

    @OneToMany(cascade=CascadeType.ALL)
    private HashMap<Long,Post> comments;

    protected User() {}

    public User(String username, String password, String email){
        this.username = username;
        this.password = password;
        this.email = email;
        this.comments = new HashMap<Long,Post>();
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

    public HashMap<Long,Post> getComments(){
        return this.comments;
    }

}
