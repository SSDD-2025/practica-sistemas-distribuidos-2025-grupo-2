package codehub.grupo2.DB.Entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private LocalDate date;
    private String text;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false) 
    private UserName user;
    
    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false) 
    private Post post;

    protected Comment() {}

    public Comment(UserName user, String text, Post post){
        this.user = user;
        this.text = text;
        this.date = LocalDate.now();
        this.post = post;
    }

    public UserName getUsername() {
        return this.user;
    }

    public LocalDate getDate() {
        return this.date;
    }

    public String getText() {
        return this.text;
    }

    public void setText(String text) { 
        this.text = text;
    }

    public Post getPost() {
        return this.post;
    }

    public Long getId() {
        return this.id;
    }
    public void setUsername(UserName user) {
        this.user = user;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setPost(Post post) {
        this.post = post;
    }
}
