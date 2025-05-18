package codehub.grupo2.DB.Entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;

@Entity
public class Topic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String topicName;

    @OneToMany(mappedBy = "topic", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Post> posts = new ArrayList<>();

    protected Topic() {}

    public Topic(String topicName) {
        this.topicName = topicName;
    }    

    public String getTopicName(){
        return this.topicName;
    }

    public List<Post> getPosts(){
        return this.posts;
    }

    public Long getId(){
        return this.id;
    }   

    @Override
    public String toString(){
        return this.topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }

    public void setId(Long id2) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setId'");
    }

}
