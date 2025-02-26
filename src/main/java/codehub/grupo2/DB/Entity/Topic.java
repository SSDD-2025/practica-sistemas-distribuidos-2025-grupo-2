package codehub.grupo2.DB.Entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;

@Entity
public class Topic {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String topicName;

    @OneToMany(cascade=CascadeType.ALL)
    private List<Post> posts;

    protected Topic() {}

    public Topic(String topicName){
        this.topicName = topicName;
        this.posts = new ArrayList<>();
    }       

    public String getTopicName(){
        return this.topicName;
    }

    public List<Post> getPosts(){
        return this.posts;
    }

    @Override
    public String toString(){
        return this.topicName;
    }
}
