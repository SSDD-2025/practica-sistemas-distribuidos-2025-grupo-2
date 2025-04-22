package codehub.grupo2.DB.Entity;

import java.sql.Blob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import jakarta.persistence.*;

@Entity
public class UserName{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String username;
    private String password;

    @Transient
    private String rawPassword;

    private String email;
    
    @Lob
    private Blob profilePicture;


    @OneToMany(cascade = CascadeType.REMOVE, fetch = FetchType.EAGER, mappedBy = "user")
    private List<Post> posts = new ArrayList<>();

    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> rol;

        public UserName() {
    }

    public UserName(String username, String password, String email){
        this.username = username;
        this.password = password;
        this.email = email;
        this.profilePicture = null;
        this.rol = new ArrayList<>();
        this.rol.add("USER");
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

    public List<String> getRoles() {
        return rol;
    }

    public void setRoles(List<String> roles) {
        this.rol = roles;
    }

    public List<Post> getPosts(){
        return this.posts;
    }

    public String getRawPassword() { 
        return rawPassword; 
    }
    public void setRawPassword(String rawPassword) { 
        this.rawPassword = rawPassword; 
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }

    public Long getId(){
        return this.id;
    }

    @Override
    public String toString() {
        return String.format("-> %s <-",username);
    }

    public void setProfilePicture(Blob profilePicture) {
        this.profilePicture = profilePicture;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public Blob getProfilePicture() {
        return this.profilePicture;
    }

        public String getProfilePictureBase64() {
        if (this.profilePicture == null) {
            return null;
        }
        try {
            byte[] bytes = this.profilePicture.getBytes(1, (int) this.profilePicture.length());
            return Base64.getEncoder().encodeToString(bytes);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }


}
