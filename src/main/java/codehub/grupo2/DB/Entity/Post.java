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

    private User username;
    private LocalDate date;
    private String title;
    private String text;

    @OneToMany(cascade=CascadeType.ALL)
    private List<Comment> comments;

    protected Post() {}

    public Post(User username, String title, String text){
        this.username = username;
        this.title = title;
        this.text = text;
        this.date = LocalDate.now();
        this.comments = new ArrayList<>();
    }

    public User getUsername(){
        return this.username;
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
