package codehub.grupo2.DB.Entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    
    private LocalDate date;
    private String title;
    private String text;

    @ManyToOne
    @JoinColumn(name="user_id")
    private UserName user;
    @OneToMany(cascade=CascadeType.ALL)
    private List<Comment> comments;

    protected Post() {}

    public Post(UserName username, String title, String text){
        this.user = username;
        this.title = title;
        this.text = text;
        this.date = LocalDate.now();
        this.comments = new ArrayList<>();
    }

    public UserName getUsername(){
        return this.user;
    }

    public LocalDate getDate(){
        return this.date;
    }   

    public String getTitle(){
        return this.title;
    }

    public String getText(){
        return this.text;
    }

    public List<Comment> getComments(){
        return this.comments;
    }
    
}
