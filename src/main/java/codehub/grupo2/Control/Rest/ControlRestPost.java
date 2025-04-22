package codehub.grupo2.Control.Rest;

import java.net.URI;
import java.util.Collection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import codehub.grupo2.Dto.PostDTO;
import codehub.grupo2.Service.APIService.APIPostService;

import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentRequest;

@RestController
@RequestMapping("/api/Posts")
public class ControlRestPost {

    @Autowired
    private APIPostService PostService;

    @GetMapping("/")
    public Collection<PostDTO> getPosts() {

        return PostService.getAllPostDTO();
    }

    @GetMapping("/{id}")
    public PostDTO getPost(@PathVariable long id) {

        return PostService.getPostByIdDTO(id);
    }

    @PostMapping("/")
    public ResponseEntity<PostDTO> createPost(@RequestBody PostDTO PostDTO) {
        PostDTO createdPost = PostService.registerPostDTO(PostDTO.user(), PostDTO.title(), PostDTO.text(), PostDTO.topic());
        
        URI location = fromCurrentRequest().path("/{id}").buildAndExpand(createdPost.id()).toUri();
        
        return ResponseEntity.created(location).body(createdPost);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<PostDTO> deletePost(@PathVariable long id) {
    PostDTO dto = PostService.getPostByIdDTO(id);
    
    if (dto == null) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(null);
    }

    PostDTO deletedPost = PostService.deletePostDTO(dto.title());
    return ResponseEntity.ok(deletedPost);
}
}
