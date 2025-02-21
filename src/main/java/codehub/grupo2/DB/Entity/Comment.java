package codehub.grupo2.DB.Entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private User username;
    private LocalDate date;
    private String title;
    private String text;

    protected Comment() {}

    public Comment(User username, String title, String text){
        this.username = username;
        this.title = title;
        this.text = text;
        this.date = LocalDate.now();
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
    
}