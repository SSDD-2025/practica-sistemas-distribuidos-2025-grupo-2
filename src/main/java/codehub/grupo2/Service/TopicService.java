package codehub.grupo2.Service;

import java.util.Optional;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import codehub.grupo2.DB.TopicRepository;
import codehub.grupo2.DB.Entity.Post;
import codehub.grupo2.DB.Entity.Topic;


@Service
public class TopicService {
    @Autowired
    private TopicRepository TopicBD;

    @Autowired
    private PostService ps;

    public Optional<Topic> getTopicById(Long id){
        if(TopicBD.existsById(id)){
            return TopicBD.findById(id);
        }
        return null;
    }

    public Topic newTopic(String name){
        Topic to = new Topic(name);
        TopicBD.save(to);
        return to;
    }
    public List<Topic> getAllTopics(){
        return TopicBD.findAll();
    }

    public void deleteTopic(Long id) {
        if (TopicBD.existsById(id)) {
            Topic topic = TopicBD.findById(id).get();
            for (Post p : topic.getPosts()) {
                ps.deletePost(p.getTitle()); 
            }
            TopicBD.deleteById(id);
        }
    }
    
}
