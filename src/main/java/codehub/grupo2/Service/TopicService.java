package codehub.grupo2.Service;

import java.util.Optional;
import java.util.Collection;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import codehub.grupo2.DB.TopicRepository;
import codehub.grupo2.DB.Entity.Post;
import codehub.grupo2.DB.Entity.Topic;
import codehub.grupo2.Dto.TopicDTO;
import codehub.grupo2.Dto.TopicMapper;


@Service
public class TopicService {

    @Autowired  
    private TopicMapper TopicMapper;


    @Autowired
    private TopicRepository TopicBD;

    @Autowired
    private PostService PostBD;

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

    @Transactional
    public void deleteTopic(long id) {
        Topic topic = TopicBD.findById(id).get();  
        for(Post t : topic.getPosts()){
            PostBD.deletePost(t.getTitle());
        }
        TopicBD.delete(topic);
    }

  
}
