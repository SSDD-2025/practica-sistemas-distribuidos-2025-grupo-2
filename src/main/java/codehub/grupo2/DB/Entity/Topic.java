package codehub.grupo2.DB.Entity;

import java.util.LinkedList;

import jakarta.persistence.*;

@Entity
public class Topic {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String topicName;

    @OneToMany(cascade=CascadeType.ALL)
    private LinkedList<Post> posts;

    protected Topic() {}

    public Topic(String topicName){
        this.topicName = topicName;
        this.posts = new LinkedList<Post>();
    }       

    public String getTopicName(){
        return this.topicName;
    }

    public LinkedList<Post> getPosts(){
        return this.posts;
    }
}
