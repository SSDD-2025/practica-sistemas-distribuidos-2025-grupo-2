package codehub.grupo2.Component;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;
import codehub.grupo2.DB.Entity.Post;


@Component
@SessionScope
public class PostComponent {
    private Post post;

    public Post getPost(){
        return this.post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public boolean isEmpty() {
        return this.post != null;
    }

    public boolean isPost(Post post) {
        return this.post.equals(post);
    }
    
}
