package codehub.grupo2.DB.Entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    
    private LocalDate date;
    private String title;
    private String text;
    @ManyToOne
    private UserName username;
    
    @ManyToOne
    private Post post;


    protected Comment() {}
    
    public Comment(UserName username, String title, String text, Post post){
        this.username = username;
        this.title = title;
        this.text = text;
        this.date = LocalDate.now();
        this.post=post;
    }

    public UserName getUsername(){
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

    public String setText(String text){
        return this.text = text;
    }
    
}