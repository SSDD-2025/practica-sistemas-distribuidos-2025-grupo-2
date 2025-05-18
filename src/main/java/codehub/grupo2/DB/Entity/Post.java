package codehub.grupo2.DB.Entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    
    private LocalDate date;
    private String title;
    private String text;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserName user;


    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
    private List<Comment> comments = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "topic_id", nullable = false) 
    private Topic topic;


    protected Post() {}

    public Post(UserName username, String title, String text, Topic topic){
        this.user = username;
        this.title = title;
        this.text = text;
        this.date = LocalDate.now();
        this.comments = new ArrayList<>();
        this.topic = topic;
    }

    public UserName getUser() {
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

    public Topic getTopic(){
        return this.topic;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Long getId() {
        return this.id;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setUser(UserName user) {
        this.user = user;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public void setTopic(Topic topic) {
        this.topic = topic;
    }

    
    
}
