package codehub.grupo2.Component;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;
import codehub.grupo2.Dto.PostDTO;

@Component
@SessionScope
public class PostComponent {
    private PostDTO post;

    public PostDTO getPost() {
        return this.post;
    }

    public void setPost(PostDTO post) {
        this.post = post;
    }

    public boolean isEmpty() {
        return this.post == null;
    }

    public boolean isPost(PostDTO post) {
        return this.post != null && this.post.equals(post);
    }
}